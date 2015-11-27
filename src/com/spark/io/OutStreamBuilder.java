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
public class OutStreamBuilder extends StreamBuilder {
    protected OutStreamBuilder(String path) throws MalformedURLException {
        super(path);
    }

    protected OutStreamBuilder(URL url) {
        super(url);
    }

    public OutStream open() {
        try {
            URLConnection connection = connect();
            int connect = getConnectTimeout();
            if (connect > 0)
                connection.setConnectTimeout(connect);
            int read = getReadTimeout();
            if (read > 0)
                connection.setReadTimeout(read);
            connection.setDoOutput(true);
            connection.setDoInput(false);
            connection.setUseCaches(isUsingCache());
            for (Map.Entry<String, String> entry : getRequestProperties().entrySet())
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            return new OutStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return OutStream.NIL;
        }
    }
}
