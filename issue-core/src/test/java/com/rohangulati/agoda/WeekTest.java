package com.rohangulati.agoda;

import com.rohangulati.agoda.data.Week;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class WeekTest {

  @Test
  public void shouldParseWeek() {
    Assert.assertNotNull(Week.of("2019W01"));
    Assert.assertNotNull(Week.of(new Date()));
    Assert.assertNotNull(Week.of(2019, 52));
    Assert.assertNotNull(Week.of(2019, 1));
  }

  @Test
  public void shouldNotParseWeek() {
    Asserts.assertException(() -> Week.of("2019W1"), new Exception("Invalid format of week"));
    Asserts.assertException(() -> Week.of((String) null), new Exception("input cannot be null"));
    Asserts.assertException(() -> Week.of(0, 0), new Exception("Invalid year [0]"));
    Asserts.assertException(() -> Week.of(-1, 0), new Exception("Invalid year [-1]"));
    Asserts.assertException(() -> Week.of(2019, -1), new Exception("Invalid week [-1]"));
    Asserts.assertException(() -> Week.of(2019, 0), new Exception("Invalid week [0]"));
  }
}
