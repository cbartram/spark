package com.spark.util;

import java.applet.Applet;
import java.net.URL;

import com.spark.io.ArchiveReader;
import com.spark.io.ArchiveReaderFactory;
import com.spark.lang.ClassCreator;
import com.spark.net.UserAgent;

import org.objectweb.asm.tree.ClassNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * InjectionAppletLoader
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
public class InjectionAppletLoader implements AppletLoader {

    @Getter
    @Setter
    private Injector injector;

    /**
     * Loads the Classes from Runescape and Passes them to the Application Class as ClassNode[] objects
     * @param configuration
     * @return
     * @throws Exception
     */
    @Override
    public Class<? extends Applet> load(final @NonNull Configuration configuration) throws Exception {
        final String initialClassName = configuration.get(Configuration.INITIAL_CLASS);
        if (initialClassName == null)
            throw new ClassNotFoundException("Unable to find initial class in configuration.");

        String gamepack = configuration.get(Configuration.INITIAL_JAR);
        if (gamepack == null)
            throw new IllegalArgumentException("No GamePack specified in configuration.");

        // Builds a url to retrieve the gamepack from the configuration (gamepack_6388569.jar) for a given world.
        ArchiveReader reader = ArchiveReaderFactory.builder()
            .url(new URL(String.format(configuration.getType().getGamepack(), configuration.getWorld(), gamepack)))
            .build()
            .property("User-Agent", UserAgent.getSystemUserAgent())
            .create();
//        ArchiveReader reader = Archive.reader(String.format(configuration.getType().getGamepack(), configuration.getWorld(), gamepack))
//            .timeout(ArchiveStreamBuilder.DEFAULT_CONNECT_TIMEOUT, ArchiveStreamBuilder.DEFAULT_CONNECT_TIMEOUT)
//            .property("User-Agent", UserAgent.getSystemUserAgent())
//            .open();

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
