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
public interface AppletLoader {
    public GameType getType();

    public int getWorld();

    public Applet load(Map<String, String> parameters) throws Exception;
}
