package com.spark.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
            return new OutStream(build().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return OutStream.NIL;
        }
    }
}
