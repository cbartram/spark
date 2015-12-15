package com.spark.io.applet;

import com.spark.applet.GameStub;
import com.spark.io.archive.Archive;
import com.spark.io.archive.ArchiveReader;
import com.spark.io.archive.ArchiveStreamBuilder;
import com.spark.lang.ClassCreator;
import com.spark.net.UserAgent;
import com.spark.util.GameType;
import com.spark.util.Injector;
import org.objectweb.asm.tree.ClassNode;

import java.applet.Applet;
import java.awt.*;
import java.util.Map;

/**
 * InjectionAppletLoader
 *
 * @author Ian
 * @version 1.0
 */
public class InjectionAppletLoader extends AbstractAppletLoader {
    private Injector injector;
    private boolean reload;

    public InjectionAppletLoader(GameType type, int world) {
        this(type, world, false);
    }

    public InjectionAppletLoader(GameType type, int world, boolean reload) {
        this(type, world, null, reload);
    }

    public InjectionAppletLoader(GameType type, int world, Injector injector) {
        this(type, world, injector, false);
    }

    public InjectionAppletLoader(GameType type, int world, Injector injector, boolean reload) {
        super(type, world);
        this.injector = injector;
        this.reload = reload;
    }

    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    public boolean isReloading() {
        return reload;
    }

    public void setReloading(boolean reload) {
        this.reload = reload;
    }

    @Override
    public Applet load(Map<String, String> configuration) throws Exception {
        if (configuration == null)
            throw new IllegalArgumentException();
        String initialClassName = configuration.get(GameStub.INITIAL_CLASS);
        if (initialClassName == null)
            throw new ClassNotFoundException("Unable to find initial class in configuration.");
        String gamepack = configuration.get(GameStub.INITIAL_JAR);
        if (gamepack == null)
            throw new IllegalArgumentException("No gamepack specified in configuration.");
        try (ArchiveReader reader = Archive.reader(String.format(getType().getGamepack(), getWorld(), gamepack))
                .timeout(ArchiveStreamBuilder.DEFAULT_CONNECT_TIMEOUT, ArchiveStreamBuilder.DEFAULT_CONNECT_TIMEOUT)
                .property("User-Agent", UserAgent.getSystemUserAgent())
                .open()) {
            ClassNode[] nodes = reader.readNodes();
            Injector injector = getInjector();
            if (injector != null)
                injector.modify(nodes);
            ClassLoader loader = new ClassCreator(nodes);
            Class<?> c = loader.loadClass(initialClassName.replace(".class", ""));
            if (!Applet.class.isAssignableFrom(c))
                throw new ClassCastException("Unable to cast initial class to Applet.");
            Applet applet = (Applet) c.newInstance();
            applet.setStub(new GameStub(applet, configuration));
            applet.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            applet.setBackground(Color.BLACK);
            applet.init();
            applet.start();
            return applet;
        }
    }
}
