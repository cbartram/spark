package com.iancaffey.spark.util;

import java.applet.Applet;

/**
 * AppletLoader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public interface AppletLoader {
    public Class<? extends Applet> load(Configuration configuration) throws Exception;
}
