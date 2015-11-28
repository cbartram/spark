package com.spark.io;

import com.spark.util.GameType;

/**
 * AbstractGamepackReader
 *
 * @author Ian
 * @version 1.0
 */
public abstract class AbstractGamepackReader implements GamepackReader {
    private final GameType type;
    private final int world;

    public AbstractGamepackReader(GameType type, int world) {
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
