package com.spark.util;

import java.applet.Applet;

/**
 * AppletLauncher
 * Creates and Launches a new Applet which loads the Runescape Game
 *
 * @author Christian Bartram
 * @since 1.0
 */
public class AppletLauncher {
    // All interfaces for loading and launching the applet
    private AppletLoader loader;
    private AppletCreator creator;
    private ConfigurationReader reader;

    public AppletLauncher(ConfigurationReader reader, AppletLoader loader, AppletCreator creator) {
        this.loader = loader;
        this.creator = creator;
        this.reader = reader;
    }

    public AppletLoader getLoader() {
        return loader;
    }

    public AppletCreator getCreator() {
        return creator;
    }

    /**
     * Given the GameType and world number this sets up the configuration for the Applet
     * @param type GameType
     * @param world int World number
     * @return Configuration object
     * @throws Exception
     */
    public Configuration configure(GameType type, int world) throws Exception {
        if (type == null)
            throw new IllegalArgumentException();
        ConfigurationReader reader = this.reader;
        if (reader == null)
            return null;
        return reader.configure(type, world);
    }

    /**
     * Launches the applet by both creating and loading it with configuration. This is directly
     * invoked by the Application class and is what is loaded into the main content frame of the JFrame GUI.
     * @param configuration Configuration Object
     * @return
     * @throws Exception
     */
    public Applet launch(Configuration configuration) throws Exception {
        if (configuration == null)
            throw new IllegalArgumentException();
        AppletLoader loader = getLoader();
        if (loader == null)
            return null;
        AppletCreator creator = getCreator();
        if (creator == null)
            return null;
        return creator.create(loader.load(configuration), configuration);
    }
}
