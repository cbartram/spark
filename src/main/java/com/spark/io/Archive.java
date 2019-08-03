package com.spark.io;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Archive
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class Archive {
    private Archive() {
    }

    public static ArchiveReaderBuilder reader(String path) throws MalformedURLException {
        return new ArchiveReaderBuilder(path);
    }

    public static ArchiveReaderBuilder reader(URL url) {
        return new ArchiveReaderBuilder(url);
    }

    public static ArchiveReaderBuilder writer(String path) throws MalformedURLException {
        return new ArchiveReaderBuilder(path);
    }

    public static ArchiveReaderBuilder writer(URL url) {
        return new ArchiveReaderBuilder(url);
    }
}
