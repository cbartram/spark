package com.spark.io;

import java.util.HashMap;
import java.util.Map;

import com.spark.net.UserAgent;
import com.spark.util.Configuration;
import com.spark.util.ConfigurationReader;
import com.spark.util.GameType;

import lombok.NonNull;

/**
 * ArchiveConfigurationReader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class ArchiveConfigurationReader implements ConfigurationReader {

    /**
     * Initializes the Configuration Class by reading the runescape configuration.
     * @param type
     * @param world
     * @return
     * @throws Exception
     */
    @Override
    public Configuration configure(@NonNull final GameType type, final int world) throws Exception {
        if (world <= 0) throw new IllegalArgumentException();
        try (ArchiveReader stream = Archive.reader(String.format(type.getConfig(), world))
                .timeout(ArchiveStreamBuilder.DEFAULT_CONNECT_TIMEOUT, ArchiveStreamBuilder.DEFAULT_CONNECT_TIMEOUT)
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
            return new Configuration(type, world, parameters);
        }
    }
}
