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

    @Getter
    @NonNull
    private final AppletLoader loader;

    @Getter
    @NonNull
    private final AppletCreator creator;

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
