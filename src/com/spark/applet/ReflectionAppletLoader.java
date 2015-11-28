package com.spark.applet;

import com.spark.util.GameType;

import java.applet.Applet;
import java.awt.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * ReflectionAppletLoader
 *
 * @author Ian
 * @version 1.0
 */
public class ReflectionAppletLoader extends AppletLoader {
    public ReflectionAppletLoader(GameType type, int world) {
        super(type, world);
    }

    @Override
    public Applet load(Map<String, String> parameters) throws Exception {
        String initialClassName = parameters.get(GameStub.INITIAL_CLASS);
        if (initialClassName == null)
            return null;
        URLClassLoader loader = new URLClassLoader(new URL[]{new URL(String.format(getType().getGamepack(), getWorld(), parameters.get(GameStub.INITIAL_JAR)))});
        Class<?> c = loader.loadClass(initialClassName.replace(".class", ""));
        if (!Applet.class.isAssignableFrom(c))
            return null;
        Applet applet = (Applet) c.newInstance();
        applet.setStub(new GameStub(applet, parameters));
        applet.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        applet.setBackground(Color.BLACK);
        applet.init();
        applet.start();
        return applet;
    }
}
