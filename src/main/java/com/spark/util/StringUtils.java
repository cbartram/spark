package com.spark.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtils {

  /**
   * Generates an 8 character unique id based on the SHA hash of a
   * randomly generated UUID.
   * @return String 8 character unique id.
   */
  public static String uniqueId() {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
      return new String(hash).substring(0, 8);
    } catch(NoSuchAlgorithmException e) {
      log.error("No such algorithm exception thrown while attempting to generate unique SHA hash.");
      return "";
    }
  }
}
