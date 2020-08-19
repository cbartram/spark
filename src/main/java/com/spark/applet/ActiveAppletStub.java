package com.spark.applet;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * ActiveAppletStub
 *
 * @author Christian Bartram
 * @since 1.0
 */
@RequiredArgsConstructor
public abstract class ActiveAppletStub implements AppletStub {

    @Getter
    @Setter
    private boolean active;

    @NonNull
    private final Applet applet;

    @Override
    public void appletResize(int width, int height) {
        applet.setSize(new Dimension(width, height));
    }
}
