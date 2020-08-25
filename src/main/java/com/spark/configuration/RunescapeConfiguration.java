package com.spark.configuration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.spark.jar.ArchiveReader;
import com.spark.jar.ArchiveReaderFactory;
import com.spark.http.UserAgent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration - Reads Configuration text that is formatted in key value pairs and delineated by newlines (\n).
 * For example. world=2\ngame_type=oldschool\n... The configuration text should be internet accessible and hosted where a
 * standard URL connection can access it. i.e No Authentication or OAuth access tokens involved.
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
@Component
public class RunescapeConfiguration {

    public static final String APPLET_MAXIMUM_HEIGHT = "applet_maxheight";
    public static final String APPLET_MAXIMUM_WIDTH = "applet_maxwidth";
    public static final String APPLET_MINIMUM_HEIGHT = "applet_minheight";
    public static final String APPLET_MINIMUM_WIDTH = "applet_minwidth";
    public static final String CODEBASE = "codebase";
    public static final String INITIAL_CLASS = "initial_class";
    public static final String INITIAL_JAR = "initial_jar";
    public static final String WINDOW_TITLE = "OS Runescape Bot";

    @Getter
    private final GameType type;

    @Getter
    private final Integer world;

    @Getter
    private final Map<String, String> parameters = new HashMap<>();

    public RunescapeConfiguration(@Value("${gametype.type}") final String gameType, @Value("${gametype.world}") final int world) {
        this.type = GameType.valueOf(gameType);
        this.world = world;

        try {
            if (world <= 0) throw new IllegalArgumentException("RuneScape Game world cannot be less than 0.");
            log.debug("Attempting to open URL connection read KV configuration from URL: {}", String.format(type.getConfig(), world));
            // Reads & parses the Key=value configuration from http://oldschool<WORLD>.runescape.com/jav_config.ws
            ArchiveReader stream = ArchiveReaderFactory.builder()
                .url(new URL(String.format(type.getConfig(), world)))
                .build()
                .property("User-Agent", UserAgent.getSystemUserAgent())
                .create();

            String[] strings = stream.readLines();
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
        } catch(IOException e) {
            log.error("IOException thrown while attempting to read key value configuration pairs from: {}", String.format(type.getConfig(), world) , e);
        }
    }

    public String get(@NonNull final String key) {
        return parameters.get(key);
    }
}
