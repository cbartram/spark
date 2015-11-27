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
public class InStreamBuilder extends StreamBuilder {
    protected InStreamBuilder(String path) throws MalformedURLException {
        super(path);
    }

    protected InStreamBuilder(URL url) {
        super(url);
    }

    public InStream open() {
        try {
            return new InStream(build().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return InStream.NIL;
        }
    }
}
