package com.spark.util;

import java.applet.Applet;

/**
 * AppletLoader
 *
 * @author Christian Bartram
 * @since 1.0
 */
public interface AppletLoader {
    public Class<? extends Applet> load(Configuration configuration) throws Exception;
}
