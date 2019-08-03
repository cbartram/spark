package com.spark.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ArchiveReaderBuilder
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class ArchiveReaderBuilder extends ArchiveStreamBuilder<ArchiveReaderBuilder> {
    ArchiveReaderBuilder(String path) throws MalformedURLException {
        super(path);
    }

    ArchiveReaderBuilder(URL url) {
        super(url);
    }

    @Override
    protected ArchiveReaderBuilder getThis() {
        return this;
    }

    public ArchiveReader open() {
        try {
            return new ArchiveReader(build().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ArchiveReader.NIL;
        }
    }
}
