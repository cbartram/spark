package com.spark.io;

import com.spark.net.UserAgent;
import com.spark.util.GameType;

import java.util.HashMap;
import java.util.Map;

/**
 * StreamConfigurationReader
 *
 * @author Ian
 * @version 1.0
 */
public class StreamConfigurationReader extends AbstractConfigurationReader {
    public StreamConfigurationReader(GameType type, int world) {
        super(type, world);
    }

    @Override
    public Map<String, String> readConfiguration() throws Exception {
        try (InStream stream = Stream.in(String.format(getType().getConfig(), getWorld()))
                .timeout(StreamBuilder.DEFAULT_CONNECT_TIMEOUT, StreamBuilder.DEFAULT_CONNECT_TIMEOUT)
                .property("User-Agent", UserAgent.getSystemUserAgent())
                .open()) {
            String[] strings = stream.readLines();
            Map<String, String> configuration = new HashMap<>();
            for (String string : strings) {
                if (!string.contains("="))
                    continue;
                int index = 0;
                if (string.startsWith("param="))
                    index += 6;
                int splitIndex = string.indexOf('=', index);
                if (splitIndex == -1)
                    continue;
                configuration.put(string.substring(index, splitIndex), string.substring(splitIndex + 1));
            }
            return configuration;
        }
    }
}
