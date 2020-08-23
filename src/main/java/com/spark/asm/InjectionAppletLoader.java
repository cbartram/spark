package com.spark.asm;

import java.applet.Applet;
import java.net.MalformedURLException;
import java.net.URL;

import com.spark.configuration.Configuration;
import com.spark.http.UserAgent;
import com.spark.jar.ArchiveReader;
import com.spark.jar.ArchiveReaderFactory;

import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * InjectionAppletLoader - Parses, reads and loads the original RuneScape JAR archive then injects the necessaru classes
 * with interfaces, getters, setters, etc... finally finds and loads the injected RuneScape class which extends the Applet
 * class. The final product of this class is loaded into the JFrame.
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
@Component
public class InjectionAppletLoader {

    @Autowired
    private Configuration configuration;

    @Autowired
    private ClassInjector classInjector;

    @Value("${jar.obfuscated.path}")
    private String obfuscatedJarPath;

    @Value("${jar.deobfuscated.path}")
    private String deobfuscatedJarPath;

    @Value("${jar.obfuscated.save}")
    private boolean saveObfuscatedJar;

    /**
     * Reads the JAR archive and retrieves all the class files contained in the JAR and
     * then creates a new ClassInjector to modify the classes with the necessary bytecode.
     * @return ClassNode Array. An array of fully injected class nodes ready to be loaded into
     *  the Applet & JFrame.
     * @throws Exception
     */
    public ClassNode[] inject() {
        ClassNode[] nodes = readArchive();

        // This is where the bytecode is actually injected.
        classInjector.modify(nodes);
        return nodes;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Applet> load() {
        ClassNode[] injectedGameClient = inject();
        ClassLoader loader = new RunescapeClassLoader(injectedGameClient);
        final String className = configuration.get(Configuration.INITIAL_CLASS).replace(".class", "");
        try {
            Class<?> c = loader.loadClass(className);
            if (!Applet.class.isAssignableFrom(c))
                throw new ClassCastException("Unable to cast initial game class to Applet. The class: " + c.getName() + " probably does not extend the Applet class.");
            return (Class<? extends Applet>) c;
        } catch(ClassNotFoundException e) {
            log.error("No cloud could be found for name: {}. Cannot load non-existent class.", className, e);
            return null;
        }
    }
    /**
     * Loads the Classes from RuneScape and Passes them to the Application Class as ClassNode[] objects
     * @return Class Returns a class which extends the Applet class.
     * @throws Exception
     */
    public ClassNode[] readArchive() {
        // Builds a url to retrieve the game pack from the configuration (gamepack_6388569.jar) for a given world.
        // The key here which makes this different from configuration ArchiveReader is that we are reading the game pack.
        // with #getGamepack() and not a string of key value pairs.
        try {
            ArchiveReader reader = ArchiveReaderFactory.builder()
                .url(new URL(String.format(
                    configuration.getType().getGamepack(),
                    configuration.getWorld(),
                    configuration.get(Configuration.INITIAL_JAR)
                )))
                .deobfuscatedPath(deobfuscatedJarPath)
                .obfuscatedPath(obfuscatedJarPath)
                .saveObfuscatedJar(saveObfuscatedJar)
                .build()
                .property("User-Agent", UserAgent.getSystemUserAgent())
                .createArchiveReader();

            return reader.readNodes(configuration.get("0"), configuration.get("-1"));
        } catch(MalformedURLException e) {
            log.error("There was an error forming a URL from the string: {}.", String.format(
                configuration.getType().getGamepack(),
                configuration.getWorld(),
                configuration.get(Configuration.INITIAL_JAR)), e);
            return new ClassNode[] {};
        } catch(Exception e) {
            log.error("There was some exception thrown while attempting to read the RuneScape JAR Archive from URL: {}.", String.format(
                configuration.getType().getGamepack(),
                configuration.getWorld(),
                configuration.get(Configuration.INITIAL_JAR)), e);
            return new ClassNode[] {};
        }
    }
}
