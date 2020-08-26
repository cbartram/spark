package com.spark.asm;

import com.spark.configuration.RunescapeConfiguration;
import com.spark.jar.JarArchiveReader;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.applet.Applet;

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
public class AppletLoader {

    @Autowired
    private RunescapeConfiguration configuration;

    @Autowired
    private ClassInjector classInjector;

    @Autowired
    private JarArchiveReader reader;

    /**
     * Reads the JAR archive and retrieves all the class files contained in the JAR and
     * then creates a new ClassInjector to modify the classes with the necessary bytecode.
     *
     * @return ClassNode Array. An array of fully injected class nodes ready to be loaded into
     * the Applet & JFrame.
     * @throws Exception
     */
    public ClassNode[] inject() {
        ClassNode[] nodes = reader.read();
        // This is where the bytecode is actually injected.
        classInjector.modify(nodes);
        return nodes;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Applet> load() {
        ClassNode[] injectedGameClient = inject();
        ClassLoader loader = new RunescapeClassLoader(injectedGameClient);
        final String className = configuration.get(RunescapeConfiguration.INITIAL_CLASS).replace(".class", "");
        try {
            Class<?> c = loader.loadClass(className);
            if (!Applet.class.isAssignableFrom(c))
                throw new ClassCastException("Unable to cast initial game class to Applet. The class: " + c.getName() + " probably does not extend the Applet class.");
            return (Class<? extends Applet>) c;
        } catch (ClassNotFoundException e) {
            log.error("No class could be found for name: {}. Cannot load non-existent class.", className, e);
            return null;
        }
    }
}

