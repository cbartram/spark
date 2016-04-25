package com.iancaffey.spark.util;

import java.util.Map;

/**
 * Configuration
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class Configuration {
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
    private final GamepackQuery query;
    private final Map<String, String> parameters;

    public Configuration(GamepackQuery query, Map<String, String> parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    public GamepackQuery query() {
        return query;
    }

    public String get(String key) {
        return parameters.get(key);
    }

    public String[] keys() {
        return parameters.keySet().toArray(new String[parameters.size()]);
    }

    public String[] values() {
        return parameters.values().toArray(new String[parameters.size()]);
    }
}
