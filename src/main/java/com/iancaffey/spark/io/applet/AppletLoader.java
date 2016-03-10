package com.iancaffey.spark.io.applet;

import com.iancaffey.spark.util.GamepackQuery;

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
