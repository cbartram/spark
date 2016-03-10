package com.spark.io;

import com.spark.util.GamepackQuery;

import java.util.Map;

/**
 * ConfigurationReader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public interface ConfigurationReader {
    public Map<String, String> readConfiguration(GamepackQuery query) throws Exception;

}
