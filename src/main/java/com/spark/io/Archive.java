package com.spark.io;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Archive
 *
 * @author Christian Bartram
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Archive {

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
