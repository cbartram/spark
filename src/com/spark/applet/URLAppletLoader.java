package com.spark.applet;

import com.spark.util.GameType;

import java.applet.Applet;
import java.awt.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * URLAppletLoader
 *
 * @author Ian
 * @version 1.0
 */
public class URLAppletLoader extends AbstractAppletLoader {
    public URLAppletLoader(GameType type, int world) {
        super(type, world);
    }

    @Override
    public Applet load(Map<String, String> parameters) throws Exception {
        String initialClassName = parameters.get(GameStub.INITIAL_CLASS);
        if (initialClassName == null)
            throw new ClassNotFoundException("Unable to find initial class in parameters.");
        String gamepack = parameters.get(GameStub.INITIAL_JAR);
        if (gamepack == null)
            throw new IllegalArgumentException("No gamepack specified in parameters.");
        URLClassLoader loader = new URLClassLoader(new URL[]{new URL(String.format(getType().getGamepack(), getWorld(), gamepack))});
        Class<?> c = loader.loadClass(initialClassName.replace(".class", ""));
        if (!Applet.class.isAssignableFrom(c))
            throw new ClassCastException("Unable to cast initial class to Applet.");
        Applet applet = (Applet) c.newInstance();
        applet.setStub(new GameStub(applet, parameters));
        applet.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        applet.setBackground(Color.BLACK);
        applet.init();
        applet.start();
        return applet;
    }
}
