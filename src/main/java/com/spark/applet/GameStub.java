package com.spark.applet;

import com.spark.util.Configuration;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.Getter;
import lombok.NonNull;

/**
 * GameStub
 *
 * @author Christian Bartram
 * @since 1.0
 */
public class GameStub extends ActiveAppletStub {
    private final Configuration configuration;

    @Getter
    private final AppletContext appletContext;

    public GameStub(Applet applet, Configuration configuration) {
        this(applet, null, configuration);
    }

    public GameStub(final Applet applet, final AppletContext context, final @NonNull Configuration configuration) {
        super(applet);
        this.appletContext = context;
        this.configuration = configuration;
        applet.setMaximumSize(new Dimension(Integer.parseInt(getParameter(Configuration.APPLET_MAXIMUM_WIDTH)), Integer.parseInt(getParameter(Configuration.APPLET_MAXIMUM_HEIGHT))));
        applet.setMinimumSize(new Dimension(Integer.parseInt(getParameter(Configuration.APPLET_MINIMUM_WIDTH)), Integer.parseInt(getParameter(Configuration.APPLET_MINIMUM_HEIGHT))));
        applet.setSize(applet.getMinimumSize());
        applet.setPreferredSize(applet.getSize());
    }

    @Override
    public URL getDocumentBase() {
        return getCodeBase();
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(getParameter(Configuration.CODEBASE));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getParameter(final String name) {
        return configuration.get(name);
    }
}
