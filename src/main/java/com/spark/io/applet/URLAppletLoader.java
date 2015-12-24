package com.spark.io.applet;

import com.spark.applet.GameStub;
import com.spark.util.GamepackQuery;

import java.applet.Applet;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * URLAppletLoader
 *
 * @author Ian
 * @version 1.0
 */
public class URLAppletLoader implements AppletLoader {
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
        URLClassLoader loader = new URLClassLoader(new URL[]{new URL(String.format(query.getType().getGamepack(), query.getWorld(), gamepack))});
        Class<?> c = loader.loadClass(initialClassName.replace(".class", ""));
        if (!Applet.class.isAssignableFrom(c))
            throw new ClassCastException("Unable to cast initial class to Applet.");
        return (Class<? extends Applet>) c;
    }
}
