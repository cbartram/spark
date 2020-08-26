package com.spark.interfaces;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractReader<T> implements InputStreamReader<T> {
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36";

    /**
     * This method is implemented by subclasses which define exactly how to read
     * the generic input stream and what to create out of it (POJO, ClassNode's etc...)
     * @return T Generic type T.
     */
    public abstract T read();

    /**
     * Opens a new input stream to the specified location. The ArchiveReader class
     * is capable of decoding a JAR file from this input stream however,
     * it can also be re-used to read the key/value text configuration from:
     * http://oldschool.runescape.com/jav_config.ws
     * @param url URL to connect to
     * @param connectionProperties Map of connection properties to add to the connection being made
     * @param connectTimeout Integer value specifying the maximum allowed time before a connect error is thrown
     * @param readTimeout Integer value specifying the maximum allowed time before a read error is thrown
     * @param useCache Boolean value representing if a cache of the data should be used
     * @return InputStream input stream of bytes from the specified host
     */
    public InputStream open(URL url, Map<String, String> connectionProperties, int connectTimeout, int readTimeout, boolean useCache) {
            log.info("Opening URL connection to host: http://{}", url.getHost() + url.getPath());
            try {
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(connectTimeout);
                connection.setReadTimeout(readTimeout);
                connection.setUseCaches(useCache);
                connection.setRequestProperty("User-Agent", USER_AGENT);
                for (Map.Entry<String, String> entry : connectionProperties.entrySet())
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                return connection.getInputStream();
            } catch (IOException e) {
                log.error("IOException thrown while opening URL connection to host: {}.", url.getHost() + ":" + url.getPort() + url.getPath(), e);
                return null;
            }
    }

    @Override
    public InputStream open(final URL url) {
       return open(url, new HashMap<>(), DEFAULT_CONNECT_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, true);
    }

    public InputStream open(final String url) {
        try {
            return open(new URL(url), new HashMap<>(), DEFAULT_CONNECT_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, true);
        } catch(MalformedURLException e) {
            log.error("The URL string provided is not a valid url. URL = {}", url, e);
            return null;
        }
    }
}
