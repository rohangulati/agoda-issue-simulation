package com.rohangulati.agoda.policy;

/** Implementation of a {@link Clock} which reads time from {@link System} */
public class SystemClock implements Clock {
  private static final SystemClock INSTANCE = new SystemClock();

  public static Clock value() {
    return INSTANCE;
  }

  private SystemClock() {
    // no instance creation
  }

  @Override
  public long currentTime() {
    // read from system
    return System.currentTimeMillis();
  }
}
