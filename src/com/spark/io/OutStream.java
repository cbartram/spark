package com.spark.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * InStream
 *
 * @author Ian
 * @version 1.0
 */
public class OutStream implements AutoCloseable {
    public static final OutStream NIL = new OutStream();
    private final OutputStream stream;

    private OutStream() {
        this(null);
    }

    protected OutStream(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public void close() {
        if (stream == null)
            return;
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
