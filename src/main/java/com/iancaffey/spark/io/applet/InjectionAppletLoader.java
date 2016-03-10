package com.iancaffey.spark.io.applet;

import com.iancaffey.spark.util.Injector;
import com.iancaffey.spark.applet.GameStub;
import com.iancaffey.spark.io.archive.Archive;
import com.iancaffey.spark.io.archive.ArchiveReader;
import com.iancaffey.spark.io.archive.ArchiveStreamBuilder;
import com.iancaffey.spark.lang.ClassCreator;
import com.iancaffey.spark.net.UserAgent;
import com.iancaffey.spark.util.GamepackQuery;
import org.objectweb.asm.tree.ClassNode;

import java.applet.Applet;
import java.util.Map;

/**
 * InjectionAppletLoader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class InjectionAppletLoader implements AppletLoader {
    private Injector injector;

    public InjectionAppletLoader(Injector injector) {
        this.injector = injector;
    }

    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Class<? extends Applet> load(GamepackQuery query, Map<String, String> configuration) throws Exception {
        if (query == null || configuration == null)
            throw new IllegalArgumentException();
        String initialClassName = configuration.get(GameStub.INITIAL_CLASS);
        if (initialClassName == null)
            throw new ClassNotFoundException("Unable to find initial class in configuration.");
        String gamepack = configuration.get(GameStub.INITIAL_JAR);
        if (gamepack == null)
            throw new IllegalArgumentException("No gamepack specified in configuration.");
        try (ArchiveReader reader = Archive.reader(String.format(query.getType().getGamepack(), query.getWorld(), gamepack))
                .timeout(ArchiveStreamBuilder.DEFAULT_CONNECT_TIMEOUT, ArchiveStreamBuilder.DEFAULT_CONNECT_TIMEOUT)
                .property("User-Agent", UserAgent.getSystemUserAgent())
                .open()) {
            ClassNode[] nodes = reader.readNodes(configuration.get("0"), configuration.get("-1"));
            Injector injector = getInjector();
            if (injector != null)
                injector.modify(nodes);
            ClassLoader loader = new ClassCreator(nodes);
            Class<?> c = loader.loadClass(initialClassName.replace(".class", ""));
            if (!Applet.class.isAssignableFrom(c))
                throw new ClassCastException("Unable to cast initial class to Applet.");
            return (Class<? extends Applet>) c;
        }
    }
}
