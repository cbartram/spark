package com.spark.io;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.spark.net.UserAgent;
import com.spark.util.Configuration;
import com.spark.util.GameType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * KeyValueConfigurationReader - Reads Configuration text that is formatted in key value pairs and delineated by newlines (\n).
 * For example. world=2\ngame_type=oldschool\n... The configuration text should be internet accessible and hosted where a
 * standard URL connection can access it. i.e No Authentication or OAuth access tokens involved.
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
@Service
public class ConfigurationService {

    @Value("${gametype.type}")
    private String gameType;

    @Value("${gametype.world}")
    private int world;

    /**
     * Initializes the Configuration Class by reading the Runescape configuration from a URL connection. The
     * configuration should be in key = value pairs delineated by new line characters (\n)
     * @return Configuration fully configured configuration object (no pun intended!)
     * @throws Exception
     */
    public Configuration readConfiguration() throws IOException {
        if (world <= 0) throw new IllegalArgumentException("RuneScape Game world cannot be less than 0.");
        final GameType type = GameType.valueOf(gameType);

        log.debug("Attempting to open URL connection read KV configuration from URL: {}", String.format(type.getConfig(), world));
        // Reads & parses the Key=value configuration from http://oldschool<WORLD>.runescape.com/jav_config.ws
         ArchiveReader stream = ArchiveReaderFactory.builder()
             .url(new URL(String.format(type.getConfig(), world)))
             .build()
             .property("User-Agent", UserAgent.getSystemUserAgent())
             .create();

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
                log.debug("Key = {} ---- Value = {}", string.substring(index, splitIndex), string.substring(splitIndex + 1));
                parameters.put(string.substring(index, splitIndex), string.substring(splitIndex + 1));
            }
            return new Configuration(type, world, parameters);
    }
}
