package com.spark.util;

/**
 * GamepackQuery
 *
 * @author Ian
 * @version 1.0
 */
public class GamepackQuery {
    private final GameType type;
    private final int world;

    public GamepackQuery(GameType type, int world) {
        if (type == null || world <= 0)
            throw new IllegalArgumentException();
        this.type = type;
        this.world = world;
    }

    public GameType getType() {
        return type;
    }

    public int getWorld() {
        return world;
    }
}
