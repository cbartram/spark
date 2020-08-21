package com.spark.util;

import java.applet.Applet;
import java.awt.*;

import com.spark.Factory;
import com.spark.applet.GameStub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * AppletLauncher
 * Creates and Launches a new Applet which loads the RuneScape Game
 *
 * @author Christian Bartram
 * @since 1.0
 */
@AllArgsConstructor
public class AppletFactory implements Factory<Applet> {

    @Getter
    @NonNull
    private final AppletLoader loader;

    @NonNull
    private final Configuration configuration;

    /**
     * Launches the applet by both creating and loading it with configuration. This is directly
     * invoked by the Application class and is what is loaded into the main content frame of the JFrame GUI.
     * @return Applet
     * @throws Exception IllegalAccessException Exception thrown if a new instance of the applet class cannot be instantiated.
     */
    public Applet create() throws Exception {
        Class<? extends Applet> c = loader.load(configuration);
        Applet applet = c.newInstance();
        applet.setStub(new GameStub(applet, configuration));
        applet.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        applet.setBackground(Color.BLACK);
        applet.init();
        applet.start();
        return applet;
    }
}
