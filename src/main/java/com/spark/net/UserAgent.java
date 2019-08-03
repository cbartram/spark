package com.spark.net;

/**
 * UserAgent
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class UserAgent {
    private UserAgent() {

    }

    public static String getSystemUserAgent() {
        return getSystemUserAgent(StandardBrowser.CHROME_46);
    }

    public static String getSystemUserAgent(Browser browser) {
        //TODO:Figure out operating system part of user agent
        return browser == null ? null : String.format(browser.getAgent(), "X11; Linux x86_64");
    }
}
