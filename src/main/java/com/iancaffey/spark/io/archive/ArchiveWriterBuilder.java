package com.iancaffey.spark.io.archive;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ArchiveWriterBuilder
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class ArchiveWriterBuilder extends ArchiveStreamBuilder<ArchiveWriterBuilder> {
    protected ArchiveWriterBuilder(String path) throws MalformedURLException {
        super(path);
    }

    protected ArchiveWriterBuilder(URL url) {
        super(url);
    }

    @Override
    protected ArchiveWriterBuilder getThis() {
        return this;
    }

    public ArchiveWriter open() {
        try {
            return new ArchiveWriter(build().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ArchiveWriter.NIL;
        }
    }
}
