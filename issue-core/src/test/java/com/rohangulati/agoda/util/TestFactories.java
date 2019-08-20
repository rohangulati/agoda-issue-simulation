package com.rohangulati.agoda.util;

import com.rohangulati.agoda.data.ChangeLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestFactories {

  private static final SimpleDateFormat YYYY_DD_MM = new SimpleDateFormat("yyyy-MM-dd");

  public static ChangeLog newChangeLog(String date, String fromState, String toState)
      throws ParseException {
    return new ChangeLog(YYYY_DD_MM.parse(date), fromState, toState);
  }
}
