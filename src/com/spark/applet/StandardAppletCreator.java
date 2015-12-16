package com.spark.applet;

import java.applet.Applet;
import java.awt.*;
import java.util.Map;

/**
 * StandardAppletCreator
 *
 * @author Ian
 * @version 1.0
 */
public class StandardAppletCreator implements AppletCreator {
    @Override
    public Applet create(Class<? extends Applet> c, Map<String, String> configuration) throws Exception {
        Applet applet = c.newInstance();
        applet.setStub(new GameStub(applet, configuration));
        applet.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        applet.setBackground(Color.BLACK);
        applet.init();
        applet.start();
        return applet;
    }
}
