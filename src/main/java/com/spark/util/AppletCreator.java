package com.spark.util;

import java.applet.Applet;

/**
 * AppletCreator
 *
 * @author Christian Bartram
 * @since 1.0
 */
public interface AppletCreator {
    Applet create(final Class<? extends Applet> clazz, final Configuration configuration) throws Exception;
}
