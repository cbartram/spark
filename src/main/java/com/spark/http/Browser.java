package com.spark.http;

/**
 * Browser interface to be implemented by all browser classes.
 * For example: there is a mock browser environment to load the game pack into.
 *
 * @author Christian Bartram
 * @since 1.0
 */
public interface Browser {
    String getAgent();
}
