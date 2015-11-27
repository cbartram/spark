package com.spark.applet;

import com.spark.util.GameType;

import java.applet.Applet;
import java.util.Map;

/**
 * AppletLoader
 *
 * @author Ian
 * @version 1.0
 */
public abstract class AppletLoader {
    private final GameType type;
    private final int world;

    protected AppletLoader(GameType type, int world) {
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

    public abstract Applet load(Map<String, String> parameters) throws Exception;
}
