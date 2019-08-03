package com.spark.util;

import java.applet.Applet;

/**
 * AppletCreator
 *
 * @author Christian Bartram
 * @since 1.0
 */
public interface AppletCreator {
    public Applet create(Class<? extends Applet> c, Configuration configuration) throws Exception;
}
