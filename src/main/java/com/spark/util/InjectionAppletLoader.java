package com.spark.util;

import com.spark.io.Archive;
import com.spark.io.ArchiveReader;
import com.spark.io.ArchiveStreamBuilder;
import com.spark.lang.ClassCreator;
import com.spark.net.UserAgent;
import org.objectweb.asm.tree.ClassNode;

import java.applet.Applet;

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

    /**
     * Loads the Classes from Runescape and Passes them to the Application Class as ClassNode[] objects
     * @param configuration
     * @return
     * @throws Exception
     */
    @Override
    public Class<? extends Applet> load(Configuration configuration) throws Exception {
        if (configuration == null)
            throw new IllegalArgumentException();
        String initialClassName = configuration.get(Configuration.INITIAL_CLASS);
        if (initialClassName == null)
            throw new ClassNotFoundException("Unable to find initial class in configuration.");
        String gamepack = configuration.get(Configuration.INITIAL_JAR);
        if (gamepack == null)
            throw new IllegalArgumentException("No gamepack specified in configuration.");
        try (ArchiveReader reader = Archive.reader(String.format(configuration.getType().getGamepack(), configuration.getWorld(), gamepack))
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
