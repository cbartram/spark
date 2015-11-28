package com.spark.io;

import com.spark.util.GameType;
import com.spark.net.UserAgent;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * GamepackReader
 *
 * @author Ian
 * @version 1.0
 */
public class GamepackReader {
    private final GameType type;
    private final int world;

    public GamepackReader(GameType type, int world) {
        if (type == null || world <= 0)
            throw new IllegalArgumentException();
        this.type = type;
        this.world = world;
    }

    public Map<String, String> readParameters() throws MalformedURLException {
        try (InStream stream = Stream.in(String.format(getType().getConfig(), getWorld()))
                .timeout(StreamBuilder.DEFAULT_CONNECT_TIMEOUT, StreamBuilder.DEFAULT_CONNECT_TIMEOUT)
                .property("User-Agent", UserAgent.getSystemUserAgent())
                .open()) {
            String[] strings = stream.readLines();
            Map<String, String> parameters = new HashMap<>();
            for (String string : strings) {
                if (!string.contains("="))
                    continue;
                int index = 0;
                if (string.startsWith("param="))
                    index += 6;
                int splitIndex = string.indexOf('=', index);
                if (splitIndex == -1)
                    continue;
                parameters.put(string.substring(index, splitIndex), string.substring(splitIndex + 1));
            }
            return parameters;
        }
    }

    public GameType getType() {
        return type;
    }

    public int getWorld() {
        return world;
    }
}
