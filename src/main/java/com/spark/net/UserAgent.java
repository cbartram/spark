package com.spark.net;

/**
 * UserAgent
 * Basic POJO getting the system user agent information from the machine
 * this code is being executed from
 * @author Christian Bartram
 * @since 1.0
 */
public class UserAgent {
    private UserAgent() {}

    public static String getSystemUserAgent() {
        return getSystemUserAgent(StandardBrowser.CHROME_46);
    }

    public static String getSystemUserAgent(Browser browser) {
        return browser == null ? null : String.format(browser.getAgent(), "X11; Linux x86_64");
    }
}
