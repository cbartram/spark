package com.spark.util;

import com.spark.io.ArchiveConfigurationReader;

import java.applet.Applet;

/**
 * AppletLauncher
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class AppletLauncher {
    private AppletLoader loader;
    private AppletCreator creator;
    private ConfigurationReader reader;

    public AppletLauncher() {

    }

    public AppletLauncher(AppletLoader loader, AppletCreator creator) {
        this(new ArchiveConfigurationReader(), loader, creator);
    }

    public AppletLauncher(ConfigurationReader reader, AppletLoader loader, AppletCreator creator) {
        this.loader = loader;
        this.creator = creator;
        this.reader = reader;
    }

    public AppletLoader getLoader() {
        return loader;
    }

    public void setLoader(AppletLoader loader) {
        this.loader = loader;
    }

    public AppletCreator getCreator() {
        return creator;
    }

    public void setCreator(AppletCreator creator) {
        this.creator = creator;
    }

    public Configuration configure(GameType type, int world) throws Exception {
        if (type == null)
            throw new IllegalArgumentException();
        ConfigurationReader reader = getReader();
        if (reader == null)
            return null;
        return reader.configure(type, world);
    }

    public Applet launch(GameType type, int world) throws Exception {
        return launch(configure(type, world));
    }

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

    public ConfigurationReader getReader() {
        return reader;
    }

    public void setReader(ConfigurationReader reader) {
        this.reader = reader;
    }
}
