package com.spark.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * UserAgent
 * Basic POJO getting the system user agent information from the machine
 * this code is being executed from
 * @author Christian Bartram
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAgent {

    public static String getSystemUserAgent() {
        return getSystemUserAgent(StandardBrowser.CHROME_46);
    }

    public static String getSystemUserAgent(Browser browser) {
        return browser == null ? null : String.format(browser.getAgent(), "X11; Linux x86_64");
    }
}
