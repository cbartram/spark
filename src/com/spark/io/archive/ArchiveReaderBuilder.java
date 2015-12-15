package com.spark.io.archive;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ArchiveReaderBuilder
 *
 * @author Ian
 * @version 1.0
 */
public class ArchiveReaderBuilder extends ArchiveStreamBuilder<ArchiveReaderBuilder> {
    protected ArchiveReaderBuilder(String path) throws MalformedURLException {
        super(path);
    }

    protected ArchiveReaderBuilder(URL url) {
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
