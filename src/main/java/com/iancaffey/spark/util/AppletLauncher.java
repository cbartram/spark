package com.iancaffey.spark.util;

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

    public Applet launch(GamepackQuery query) throws Exception {
        if (query == null)
            throw new IllegalArgumentException();
        ConfigurationReader reader = getReader();
        if (reader == null)
            return null;
        AppletLoader loader = getLoader();
        if (loader == null)
            return null;
        AppletCreator creator = getCreator();
        if (creator == null)
            return null;
        Configuration configuration = reader.configure(query);
        return creator.create(loader.load(configuration), configuration);
    }

    public ConfigurationReader getReader() {
        return reader;
    }

    public void setReader(ConfigurationReader reader) {
        this.reader = reader;
    }
}
