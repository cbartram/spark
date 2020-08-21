package com.spark.io;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.spark.Factory;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * ArchiveReaderFactory Creates a new instance of an ArchiveReader class
 * which can parse through a JAR file and read the individual classes and bytecode into
 * use-able class nodes by ASM. This class handles opening a URL connection to the hosted Jar file
 * and reading the input stream for the Jar Archive.
 *
 * This class leverages the builder and factory design patterns.
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
@Builder
public class ArchiveReaderFactory implements Factory<ArchiveReader> {
    public static final int DEFAULT_CONNECT_TIMEOUT = 10000;

    private final URL url;
    private final Map<String, String> requestProperties = new HashMap<>();
    private final int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private final int readTimeout = DEFAULT_CONNECT_TIMEOUT;
    private final boolean cache;

    @Getter
    private final Proxy proxy;

    public ArchiveReaderFactory property(String key, String value) {
        requestProperties.put(key, value);
        return this;
    }

    /**
     * Creates a new instance of the ArchiveReader class from a stream of input bytes. The ArchiveReader class
     *  is capable of decoding a JAR file.
     * @return ArchiveReader reader instance.
     */
    public ArchiveReader create() {
        try {
            log.debug("Opening URL connection to: {} to retrieve JAR archive.", url.getHost() + url.getPath());
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setUseCaches(cache);
            for (Map.Entry<String, String> entry : requestProperties.entrySet())
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            return new ArchiveReader(connection.getInputStream());
        } catch (IOException e) {
            log.error("IOException thrown while opening URL connection to retrieve GamePack Jar: {}.", url.getHost() + ":" + url.getPort() + url.getPath(), e);
            return null;
        }
    }
}
