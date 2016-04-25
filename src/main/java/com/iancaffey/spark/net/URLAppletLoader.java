package com.iancaffey.spark.net;

import com.iancaffey.spark.applet.GameStub;
import com.iancaffey.spark.util.AppletLoader;
import com.iancaffey.spark.util.Configuration;

import java.applet.Applet;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * URLAppletLoader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class URLAppletLoader implements AppletLoader {
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
        URLClassLoader loader = new URLClassLoader(new URL[]{new URL(String.format(configuration.query().getType().getGamepack(), configuration.query().getWorld(), gamepack))});
        Class<?> c = loader.loadClass(initialClassName.replace(".class", ""));
        if (!Applet.class.isAssignableFrom(c))
            throw new ClassCastException("Unable to cast initial class to Applet.");
        return (Class<? extends Applet>) c;
    }
}
