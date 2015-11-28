package com.spark.applet;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;

/**
 * AbstractAppletStub
 *
 * @author Ian
 * @version 1.0
 */
public abstract class AbstractAppletStub implements AppletStub {
    private boolean active;
    private Applet applet;

    protected AbstractAppletStub(Applet applet) {
        if (applet == null)
            throw new IllegalArgumentException();
        this.applet = applet;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void appletResize(int width, int height) {
        applet.setSize(new Dimension(width, height));
    }
}
