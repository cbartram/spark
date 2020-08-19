package com.spark.util;

/**
 * ConfigurationReader
 *
 * @author Christian Bartram
 * @since 1.0
 */
public interface ConfigurationReader {
    Configuration configure(final GameType type, final int world) throws Exception;
}
