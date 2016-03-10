package com.spark.applet;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * GameStub
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class GameStub extends ActiveAppletStub {
    public static final String APPLET_MAXIMUM_HEIGHT = "applet_maxheight";
    public static final String APPLET_MAXIMUM_WIDTH = "applet_maxwidth";
    public static final String APPLET_MINIMUM_HEIGHT = "applet_minheight";
    public static final String APPLET_MINIMUM_WIDTH = "applet_minwidth";
    public static final String CACHE_DIRECTORY = "cachedir";
    public static final String CODEBASE = "codebase";
    public static final String INITIAL_CLASS = "initial_class";
    public static final String INITIAL_JAR = "initial_jar";
    public static final String WINDOW_PREFERRED_HEIGHT = "window_preferredheight";
    public static final String WINDOW_PREFERRED_WIDTH = "window_preferredwidth";
    public static final String WINDOW_TITLE = "title";
    private final Map<String, String> configuration;
    private final AppletContext context;

    public GameStub(Applet applet, Map<String, String> configuration) {
        this(applet, null, configuration);
    }

    public GameStub(Applet applet, AppletContext context, Map<String, String> configuration) {
        super(applet);
        if (configuration == null)
            throw new IllegalArgumentException();
        this.context = context;
        this.configuration = configuration;
        applet.setMaximumSize(new Dimension(Integer.parseInt(getParameter(APPLET_MAXIMUM_WIDTH)), Integer.parseInt(getParameter(APPLET_MAXIMUM_HEIGHT))));
        applet.setMinimumSize(new Dimension(Integer.parseInt(getParameter(APPLET_MINIMUM_WIDTH)), Integer.parseInt(getParameter(APPLET_MINIMUM_HEIGHT))));
        applet.setSize(applet.getMinimumSize());
        applet.setPreferredSize(applet.getSize());
    }

    @Override
    public URL getDocumentBase() {
        return getCodeBase();
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(getParameter(CODEBASE));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getParameter(String name) {
        return configuration.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return context;
    }
}
