package com.iancaffey.spark.util;

/**
 * ConfigurationReader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public interface ConfigurationReader {
    public Configuration configure(GameType type, int world) throws Exception;

}
