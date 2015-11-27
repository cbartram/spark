package com.spark.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * InStreamBuilder
 *
 * @author Ian
 * @version 1.0
 */
public class InStreamBuilder extends StreamBuilder {
    protected InStreamBuilder(String path) throws MalformedURLException {
        super(path);
    }

    protected InStreamBuilder(URL url) {
        super(url);
    }

    public InStream open() {
        try {
            URLConnection connection = connect();
            int connect = getConnectTimeout();
            if (connect > 0)
                connection.setConnectTimeout(connect);
            int read = getReadTimeout();
            if (read > 0)
                connection.setReadTimeout(read);
            connection.setUseCaches(isUsingCache());
            for (Map.Entry<String, String> entry : getRequestProperties().entrySet())
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            return new InStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return InStream.NIL;
        }
    }
}
