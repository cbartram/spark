package com.iancaffey.spark.util;

/**
 * GameType
 *
 * @author Ian Caffey
 * @since 1.0
 */
public enum GameType {
    OLDSCHOOL("http://oldschool%d.runescape.com/jav_config.ws", "http://oldschool%d.runescape.com/%s"),
    RS3("http://world%d.runescape.com/jav_config.ws", "http://world%d.runescape.com/%s");
    private final String config;
    private final String gamepack;

    GameType(String config, String gamepack) {
        this.config = config;
        this.gamepack = gamepack;
    }

    public String getConfig() {
        return config;
    }

    public String getGamepack() {
        return gamepack;
    }
}
