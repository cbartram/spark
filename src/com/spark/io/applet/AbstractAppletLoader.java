package com.spark.io.applet;

import com.spark.util.GameType;

/**
 * AbstractAppletLoader
 *
 * @author Ian
 * @version 1.0
 */
public abstract class AbstractAppletLoader implements AppletLoader {
    private final GameType type;
    private final int world;

    protected AbstractAppletLoader(GameType type, int world) {
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
