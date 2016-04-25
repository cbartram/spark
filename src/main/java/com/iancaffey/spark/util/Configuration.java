package com.iancaffey.spark.util;

import java.util.Map;

/**
 * Configuration
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class Configuration {
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
