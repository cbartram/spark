package com.spark.io;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Stream
 *
 * @author Ian
 * @version 1.0
 */
public class Stream {
    private Stream() {

    }

    public static InStreamBuilder in(String path) throws MalformedURLException {
        return new InStreamBuilder(path);
    }

    public static InStreamBuilder in(URL url) {
        return new InStreamBuilder(url);
    }

    public static InStreamBuilder out(String path) throws MalformedURLException {
        return new InStreamBuilder(path);
    }

    public static InStreamBuilder out(URL url) {
        return new InStreamBuilder(url);
    }
}
