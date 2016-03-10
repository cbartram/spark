package com.spark.applet;

import java.applet.Applet;
import java.util.Map;

/**
 * AppletCreator
 *
 * @author Ian Caffey
 * @since 1.0
 */
public interface AppletCreator {
    public Applet create(Class<? extends Applet> c, Map<String, String> configuration) throws Exception;
}
