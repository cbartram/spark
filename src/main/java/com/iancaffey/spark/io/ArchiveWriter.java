package com.iancaffey.spark.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * ArchiveWriter
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class ArchiveWriter implements AutoCloseable {
    public static final ArchiveWriter NIL = new ArchiveWriter();
    private final OutputStream stream;

    private ArchiveWriter() {
        this(null);
    }

    public ArchiveWriter(OutputStream stream) {
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