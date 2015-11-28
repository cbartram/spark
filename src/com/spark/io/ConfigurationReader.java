package com.spark.io;

import com.spark.util.GameType;

import java.util.Map;

/**
 * ConfigurationReader
 *
 * @author Ian
 * @version 1.0
 */
public interface ConfigurationReader {
    public Map<String, String> readConfiguration() throws Exception;

    public GameType getType();

    public int getWorld();
}
