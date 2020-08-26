package com.spark.interfaces;

import java.io.InputStream;
import java.net.URL;

/**
 * InputStreamReader - This interface should be implemented by any class who opens a URL connection and reads an input stream
 * of any kind. The generic type T should indicate the type of data being read from the input stream. If the data being read
 * is a JAR file type T may be a ClassNode[], if the data is text T may be a POJO or String[] etc...
 * @param <T>
 */
public interface InputStreamReader<T> {
  T read();

  InputStream open(final URL url);
}