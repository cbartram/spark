package com.spark.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * InStream
 *
 * @author Ian
 * @version 1.0
 */
public class OutStream {
    public static final OutStream NIL = new OutStream();
    private final OutputStream stream;

    private OutStream() {
        this(null);
    }

    protected OutStream(OutputStream stream) {
        this.stream = stream;
    }

    public boolean close() {
        if (stream == null)
            return false;
        try {
            stream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
