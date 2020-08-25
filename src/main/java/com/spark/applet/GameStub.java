package com.spark.applet;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

import com.spark.configuration.RunescapeConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * GameStub
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
@Component
public class GameStub implements AppletStub {

    @Autowired
    private RunescapeConfiguration configuration;

    @Getter
    private AppletContext appletContext;

    @Getter
    @Setter
    private boolean active;

    @Setter
    private Applet applet;

    @Override
    public void appletResize(final int width, final int height) {
        applet.setSize(new Dimension(width, height));
    }

    @Override
    public URL getDocumentBase() {
        return getCodeBase();
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(getParameter(RunescapeConfiguration.CODEBASE));
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
