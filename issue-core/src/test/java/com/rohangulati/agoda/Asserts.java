package com.rohangulati.agoda;

import org.junit.Assert;

public class Asserts {

  private Asserts() {
    // no instance creation
  }

  public static void assertException(Runnable runnable, Exception expected) {
    Exception actual = null;
    try {
      runnable.run();
    } catch (Exception e) {
      actual = e;
    }
    Assert.assertNotNull(actual);
    Assert.assertEquals(expected.getMessage(), actual.getMessage());
  }
}
