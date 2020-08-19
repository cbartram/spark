package com.spark.util;

import java.applet.Applet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * AppletLauncher
 * Creates and Launches a new Applet which loads the Runescape Game
 *
 * @author Christian Bartram
 * @since 1.0
 */
@AllArgsConstructor
public class AppletLauncher {

    @NonNull
    private final ConfigurationReader reader;

    @Getter
    @NonNull
    private final AppletLoader loader;

    @Getter
    @NonNull
    private final AppletCreator creator;

    /**
     * Given the GameType and world number this sets up the configuration for the Applet
     * @param type GameType
     * @param world int World number
     * @return Configuration object
     * @throws Exception
     */
    public Configuration configure(final @NonNull GameType type, final int world) throws Exception {
        return reader.configure(type, world);
    }

    /**
     * Launches the applet by both creating and loading it with configuration. This is directly
     * invoked by the Application class and is what is loaded into the main content frame of the JFrame GUI.
     * @param configuration Configuration Object
     * @return
     * @throws Exception
     */
    public Applet launch(final @NonNull Configuration configuration) throws Exception {
        return creator.create(loader.load(configuration), configuration);
    }
}
