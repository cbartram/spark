package com.spark.io;

import com.spark.util.GameType;

/**
 * AbstractConfigurationReader
 *
 * @author Ian
 * @version 1.0
 */
public abstract class AbstractConfigurationReader implements ConfigurationReader {
    private final GameType type;
    private final int world;

    public AbstractConfigurationReader(GameType type, int world) {
        if (type == null || world <= 0)
            throw new IllegalArgumentException();
        this.type = type;
        this.world = world;
    }

    @Override
    public GameType getType() {
        return type;
    }

    @Override
    public int getWorld() {
        return world;
    }
}
