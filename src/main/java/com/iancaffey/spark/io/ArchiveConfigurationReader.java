package com.iancaffey.spark.io;

import com.iancaffey.spark.io.Archive;
import com.iancaffey.spark.io.ArchiveReader;
import com.iancaffey.spark.io.ArchiveStreamBuilder;
import com.iancaffey.spark.net.UserAgent;
import com.iancaffey.spark.util.Configuration;
import com.iancaffey.spark.util.ConfigurationReader;
import com.iancaffey.spark.util.GamepackQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * ArchiveConfigurationReader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class ArchiveConfigurationReader implements ConfigurationReader {
    @Override
    public Configuration configure(GamepackQuery query) throws Exception {
        if (query == null)
            throw new IllegalArgumentException();
        try (ArchiveReader stream = Archive.reader(String.format(query.getType().getConfig(), query.getWorld()))
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
            return new Configuration(query, parameters);
        }
    }
}
