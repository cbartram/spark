package com.iancaffey.spark.applet;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;

/**
 * ActiveAppletStub
 *
 * @author Ian Caffey
 * @since 1.0
 */
public abstract class ActiveAppletStub implements AppletStub {
    private boolean active;
    private Applet applet;

    protected ActiveAppletStub(Applet applet) {
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
