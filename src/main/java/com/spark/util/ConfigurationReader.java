package com.spark.util;

/**
 * ConfigurationReader
 *
 * @author Christian Bartram
 * @since 1.0
 */
public interface ConfigurationReader {
    public Configuration configure(GameType type, int world) throws Exception;
}
