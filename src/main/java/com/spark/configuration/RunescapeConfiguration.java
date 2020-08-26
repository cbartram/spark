package com.spark.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Configuration - Reads Configuration text that is formatted in key value pairs and delineated by newlines (\n).
 * For example. world=2\ngame_type=oldschool\n... The configuration text should be internet accessible and hosted where a
 * standard URL connection can access it. i.e No Authentication or OAuth access tokens involved.
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
@Component
public class RunescapeConfiguration {

    // These static strings represent keys in the runscape configuration
    // They are used in conjunction with the #get() method to retrieve the actual config values
    public static final String APPLET_MAXIMUM_HEIGHT = "applet_maxheight";
    public static final String APPLET_MAXIMUM_WIDTH = "applet_maxwidth";
    public static final String APPLET_MINIMUM_HEIGHT = "applet_minheight";
    public static final String APPLET_MINIMUM_WIDTH = "applet_minwidth";
    public static final String CODEBASE = "codebase";
    public static final String INITIAL_CLASS = "initial_class";
    public static final String INITIAL_JAR = "initial_jar";
    public static final String WINDOW_TITLE = "OS Runescape Bot";

    @Getter
    private final Map<String, String> parameters;

    public RunescapeConfiguration(final RunescapeConfigurationReader reader) {
        this.parameters = reader.read();
    }

    public String get(@NonNull final String key) {
        return parameters.get(key);
    }
}
