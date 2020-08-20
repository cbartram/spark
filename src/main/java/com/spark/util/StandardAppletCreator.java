package com.spark.util;

import java.applet.Applet;
import java.awt.*;

import com.spark.applet.GameStub;

import org.springframework.stereotype.Component;

/**
 * StandardAppletCreator
 * Creates a basic Apples given a Class and Configuration object. The Runescape game will
 * be loaded into the applet created by this Class.
 * @author Christian Bartram
 * @since 1.0
 */
@Component
public class StandardAppletCreator implements AppletCreator {

    @Override
    public Applet create(Class<? extends Applet> c, final Configuration configuration) throws Exception {
        Applet applet = c.newInstance();
        applet.setStub(new GameStub(applet, configuration));
        applet.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        applet.setBackground(Color.BLACK);
        applet.init();
        applet.start();
        return applet;
    }
}
