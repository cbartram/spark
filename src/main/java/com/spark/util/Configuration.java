package com.spark.util;

import java.util.Map;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Configuration
 * Basic POJO class which contains game constants and configuration
 * for the Runescape Bot
 *
 * @author Christian Bartram
 * @since 1.0
 */
@RequiredArgsConstructor
public class Configuration {
    public static final String APPLET_MAXIMUM_HEIGHT = "applet_maxheight";
    public static final String APPLET_MAXIMUM_WIDTH = "applet_maxwidth";
    public static final String APPLET_MINIMUM_HEIGHT = "applet_minheight";
    public static final String APPLET_MINIMUM_WIDTH = "applet_minwidth";
    public static final String CODEBASE = "codebase";
    public static final String INITIAL_CLASS = "initial_class";
    public static final String INITIAL_JAR = "initial_jar";
    public static final String WINDOW_TITLE = "OS Runescape Bot";

    @NonNull
    @Getter
    private final GameType type;

    @NonNull
    @Getter
    private final int world;

    @NonNull
    private final Map<String, String> parameters;

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
