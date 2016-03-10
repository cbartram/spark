package com.spark.io.applet;

import com.spark.util.GamepackQuery;

import java.applet.Applet;
import java.util.Map;

/**
 * AppletLoader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public interface AppletLoader {
    public Class<? extends Applet> load(GamepackQuery query, Map<String, String> configuration) throws Exception;
}
