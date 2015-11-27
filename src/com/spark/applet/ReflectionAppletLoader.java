package com.spark.applet;

import com.spark.io.GamepackReader;
import com.spark.util.GameType;

import javax.swing.*;
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

    public static void main(String[] args) throws Exception {
        GamepackReader reader = new GamepackReader(GameType.OLDSCHOOL, 1);
        AppletLoader loader = new ReflectionAppletLoader(reader.getType(), reader.getWorld());
        Map<String, String> parameters = reader.readParameters();
        JFrame frame = new JFrame(parameters.get(GameStub.WINDOW_TITLE));
        frame.setPreferredSize(new Dimension(Integer.parseInt(parameters.get(GameStub.WINDOW_PREFERRED_WIDTH)), Integer.parseInt(parameters.get(GameStub.WINDOW_PREFERRED_HEIGHT))));
        Applet applet = loader.load(parameters);
        frame.setContentPane(applet);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
        applet.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        applet.setBackground(Color.BLACK);
        applet.setStub(new GameStub(applet, parameters));
        System.out.println(applet);
        applet.init();
        applet.start();
        applet.setVisible(true);
        System.out.println(applet);
        return applet;
    }
}
