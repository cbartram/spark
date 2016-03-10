package com.iancaffey.spark.net;

/**
 * StandardBrowser
 *
 * @author Ian Caffey
 * @since 1.0
 */
public enum StandardBrowser implements Browser {
    CHROME_46("Mozilla/5.0 (%s) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
    private final String agent;

    StandardBrowser(String agent) {
        this.agent = agent;
    }

    @Override
    public String getAgent() {
        return agent;
    }
}
