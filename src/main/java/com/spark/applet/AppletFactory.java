package com.spark.applet;

import java.applet.Applet;
import java.awt.*;

import com.spark.Factory;
import com.spark.asm.InjectionAppletLoader;
import com.spark.configuration.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

/**
 * AppletLauncher
 * Creates and Launches a new Applet which loads the RuneScape Game
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Component
@AllArgsConstructor
public class AppletFactory implements Factory<Applet> {

    @Autowired
    private final Configuration configuration;

    @Autowired
    private final InjectionAppletLoader loader;

    @Autowired
    private final GameStub gameStub;

    /**
     * Launches the applet by both creating and loading it with configuration. This is directly
     * invoked by the Application class and is what is loaded into the main content frame of the JFrame GUI.
     * @return Applet
     * @throws Exception IllegalAccessException Exception thrown if a new instance of the applet class cannot be instantiated.
     */
    public Applet create() throws Exception {
        Class<? extends Applet> c = loader.load();
        Applet applet = c.newInstance();
        gameStub.setApplet(applet);
        applet.setStub(gameStub);
        applet.setMaximumSize(new Dimension(Integer.parseInt(configuration.get(Configuration.APPLET_MAXIMUM_WIDTH)), Integer.parseInt(configuration.get(Configuration.APPLET_MAXIMUM_HEIGHT))));
        applet.setMinimumSize(new Dimension(Integer.parseInt(configuration.get(Configuration.APPLET_MINIMUM_WIDTH)), Integer.parseInt(configuration.get(Configuration.APPLET_MINIMUM_HEIGHT))));
        applet.setSize(applet.getMinimumSize());
        applet.setPreferredSize(applet.getSize());
        applet.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        applet.setBackground(Color.BLACK);
        applet.init();
        applet.start();
        return applet;
    }
}