package com.spark.configuration;

import com.spark.interfaces.AbstractReader;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@NoArgsConstructor
public class RunescapeConfigurationReader extends AbstractReader<Map<String, String>> {

    @Value("${gametype.type}")
    private GameType type;

    @Value("${gametype.world}")
    private int world;

    private final Map<String, String> configurationParameters = new HashMap<>();

    /**
     * Opens a URL connection to the RuneScape configuration at http://oldschool.runescape.com/jav_config.ws. The configuration
     * is in the form of K=V text pairs. Note: The configuration is NOT json so a traditional ObjectMapper cannot be used here.
     * @return String[]
     */
    @Override
    public Map<String, String> read() {
        InputStream stream = null;
        try {
            if (world <= 0) throw new IllegalArgumentException("RuneScape Game world cannot be less than 0.");
            log.debug("Attempting to open URL connection read KV configuration from URL: {}", String.format(type.getConfig(), world));

            // Reads & parses the Key=value configuration from http://oldschool<WORLD>.runescape.com/jav_config.ws
            stream = open(new URL(String.format(type.getConfig(), world)));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String string;
            while ((string = reader.readLine()) != null) {
                if (!string.contains("="))
                    continue;
                int index = 0;
                if (string.startsWith("param="))
                    index += 6;
                int splitIndex = string.indexOf('=', index);
                if (splitIndex == -1)
                    continue;
                configurationParameters.put(string.substring(index, splitIndex), string.substring(splitIndex + 1));
            }
            log.info("Config params: {}", configurationParameters);
            return configurationParameters;
        } catch(IOException e) {
            log.error("IOException thrown while attempting to read key value configuration pairs from: {}", String.format(type.getConfig(), world) , e);
        }  finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("Failed to close IO connection to host: {}", String.format(type.getConfig(), world), e);
                }
            } else {
                log.warn("Connection object while opening URL connection was null. Nothing to close.");
            }
        }
        return new HashMap<>();
    }
}
