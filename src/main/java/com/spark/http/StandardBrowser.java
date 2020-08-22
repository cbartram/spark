package com.spark.http;

import lombok.AllArgsConstructor;

/**
 * StandardBrowser
 * Sets up a mock browser environment so that the Applet can run the gamepack in.
 * @author Christian Bartram
 * @since 1.0
 */
@AllArgsConstructor
public enum StandardBrowser implements Browser {
    CHROME_46("Mozilla/5.0 (%s) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
    private final String agent;

    @Override
    public String getAgent() {
        return agent;
    }
}
