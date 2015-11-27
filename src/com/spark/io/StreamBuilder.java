package com.spark.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * ConnectionBuilder
 *
 * @author Ian
 * @version 1.0
 */
public class StreamBuilder {
    public static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private final URL url;
    private final Map<String, String> requests = new HashMap<>();
    private int connect = -1;
    private int read = -1;
    private boolean cache;
    private Proxy proxy;

    protected StreamBuilder(String path) throws MalformedURLException {
        this(new URL(path));
    }

    protected StreamBuilder(URL url) {
        if (url == null)
            throw new IllegalArgumentException();
        this.url = url;
    }

    public int getConnectTimeout() {
        return connect;
    }

    public int getReadTimeout() {
        return read;
    }

    public Map<String, String> getRequestProperties() {
        return new HashMap<>(requests);
    }

    public boolean isUsingCache() {
        return cache;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public StreamBuilder proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public StreamBuilder cache(boolean useCache) {
        this.cache = useCache;
        return this;
    }

    public StreamBuilder timeout(int read) {
        return timeout(DEFAULT_CONNECT_TIMEOUT, read);
    }

    public StreamBuilder timeout(int connect, int read) {
        this.connect = connect;
        this.read = read;
        return this;
    }

    public StreamBuilder property(Map<String, String> properties) {
        if (properties != null)
            requests.putAll(properties);
        return this;
    }

    public StreamBuilder property(String key, String value) {
        requests.put(key, value);
        return this;
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
