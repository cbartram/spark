package com.spark.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * GameType
 *
 * @author Ian Caffey
 * @since 1.0
 */
@AllArgsConstructor
public enum GameType {
    OLDSCHOOL("http://oldschool%d.runescape.com/jav_config.ws", "http://oldschool%d.runescape.com/%s"),
    RS3("http://world%d.runescape.com/jav_config.ws", "http://world%d.runescape.com/%s");

    @Getter
    private final String config;

    @Getter
    private final String gamepack;
}
