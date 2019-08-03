package com.spark.util;

import com.spark.applet.GameStub;

import java.applet.Applet;
import java.awt.*;

/**
 * StandardAppletCreator
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class StandardAppletCreator implements AppletCreator {
    @Override
    public Applet create(Class<? extends Applet> c, Configuration configuration) throws Exception {
        Applet applet = c.newInstance();
        applet.setStub(new GameStub(applet, configuration));
        applet.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        applet.setBackground(Color.BLACK);
        applet.init();
        applet.start();
        return applet;
    }
}
