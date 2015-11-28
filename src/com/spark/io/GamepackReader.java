package com.spark.io;

import com.spark.util.GameType;

import java.util.Map;

/**
 * GamepackReader
 *
 * @author Ian
 * @version 1.0
 */
public interface GamepackReader {
    public Map<String, String> readParameters() throws Exception;

    public GameType getType();

    public int getWorld();
}
