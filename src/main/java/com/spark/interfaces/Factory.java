package com.spark.interfaces;

/**
 * Interface implemented by all factories which force
 * each factory to create a new object of generic type T.
 *
 * This interface allows for factories to throw exceptions while creating their object.
 * @param <T> Object to create
 */
public interface Factory<T> {
  T create() throws Exception;
}
