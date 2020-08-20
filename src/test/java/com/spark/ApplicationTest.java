package com.spark;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ApplicationTest {

  @Test
  void application_applicationLoads_success() {
    assertEquals(1, 1);
  }
}
