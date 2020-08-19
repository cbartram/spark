package com.spark.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * ConnectionBuilder
 *
 * @author Christian Bartram
 * @since 1.0
 */
public abstract class ArchiveStreamBuilder<S extends ArchiveStreamBuilder> {
    public static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private final URL url;


    @Getter
    private final Map<String, String> requestProperties = new HashMap<>();

    @Getter
    private int connectTimeout = -1;

    @Getter
    private int readTimeout = -1;
    private boolean cache;

    @Getter
    private Proxy proxy;

    ArchiveStreamBuilder(String path) throws MalformedURLException {
        this(new URL(path));
    }

    ArchiveStreamBuilder(URL url) {
        if (url == null)
            throw new IllegalArgumentException();
        this.url = url;
    }

    protected abstract S getThis();

    public boolean isUsingCache() {
        return cache;
    }

    public S proxy(Proxy proxy) {
        this.proxy = proxy;
        return getThis();
    }

    public S cache(boolean useCache) {
        this.cache = useCache;
        return getThis();
    }

    public S timeout(int read) {
        return timeout(DEFAULT_CONNECT_TIMEOUT, read);
    }

    public S timeout(int connect, int read) {
        this.connectTimeout = connect;
        this.readTimeout = read;
        return getThis();
    }

    public S property(Map<String, String> properties) {
        if (properties != null)
            requestProperties.putAll(properties);
        return getThis();
    }

    public S property(String key, String value) {
        requestProperties.put(key, value);
        return getThis();
    }

    protected URLConnection build() throws IOException {
        URLConnection connection = url.openConnection();
        int connect = getConnectTimeout();
        if (connect > 0)
            connection.setConnectTimeout(connect);
        int read = getReadTimeout();
        if (read > 0)
            connection.setReadTimeout(read);
        connection.setUseCaches(isUsingCache());
        for (Map.Entry<String, String> entry : getRequestProperties().entrySet())
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        return connection;
    }
}
