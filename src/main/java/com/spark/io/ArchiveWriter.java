package com.spark.io;

import java.io.IOException;
import java.io.OutputStream;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * ArchiveWriter
 *
 * @author Ian Caffey
 * @since 1.0
 */
@RequiredArgsConstructor
public class ArchiveWriter implements AutoCloseable {

    @NonNull
    private final OutputStream stream;

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
