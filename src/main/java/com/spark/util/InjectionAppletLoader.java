package com.spark.util;

import java.applet.Applet;
import java.net.URL;

import com.spark.io.ArchiveReader;
import com.spark.io.ArchiveReaderFactory;
import com.spark.lang.ClassCreator;
import com.spark.net.UserAgent;

import org.objectweb.asm.tree.ClassNode;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * InjectionAppletLoader
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class InjectionAppletLoader implements AppletLoader {

    /**
     * Loads the Classes from RuneScape and Passes them to the Application Class as ClassNode[] objects
     * @param configuration Configuration fully set configuration object.
     * @return Class Returns a class which extends the Applet class.
     * @throws Exception
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Applet> load(final @NonNull Configuration configuration) throws Exception {
        final String initialClassName = configuration.get(Configuration.INITIAL_CLASS);
        if (initialClassName == null)
            throw new ClassNotFoundException("Unable to find initial class in configuration.");

        String gamepack = configuration.get(Configuration.INITIAL_JAR);
        if (gamepack == null)
            throw new IllegalArgumentException("No GamePack specified in configuration.");

        // Builds a url to retrieve the game pack from the configuration (gamepack_6388569.jar) for a given world.
        // The key here which makes this different from configuration ArchiveReader is that we are reading the game pack.
        // with #getGamepack() and not a string of key value pairs.
        ArchiveReader reader = ArchiveReaderFactory.builder()
            .url(new URL(String.format(configuration.getType().getGamepack(), configuration.getWorld(), gamepack)))
            .build()
            .property("User-Agent", UserAgent.getSystemUserAgent())
            .create();


        // Classes are parsed from JAR archive and injected with accessor & mutator methods.
        ClassNode[] nodes = reader.readNodes(configuration.get("0"), configuration.get("-1"));
        new ClassInjector().modify(nodes);

        ClassLoader loader = new ClassCreator(nodes);
        Class<?> c = loader.loadClass(initialClassName.replace(".class", ""));
        if (!Applet.class.isAssignableFrom(c))
            throw new ClassCastException("Unable to cast initial class to Applet.");
        return (Class<? extends Applet>) c;
    }
}
