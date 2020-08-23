package com.spark.util;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtils {

  /**
   * Generates an 8 character unique id based on the SHA hash of a
   * randomly generated UUID.
   * @return String 8 character unique id.
   */
  public static String uniqueId() {
      int leftLimit = 48; // numeral '0'
      int rightLimit = 122; // letter 'z'
      int targetStringLength = 8;
      Random random = new Random();

      return random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
  }
}
