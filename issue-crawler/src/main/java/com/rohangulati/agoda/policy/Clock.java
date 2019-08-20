package com.rohangulati.agoda.policy;

/** Abstracts a clock in a system */
public interface Clock {

  /**
   * Return the current time of the system
   *
   * @return time in milliseconds
   */
  long currentTime();
}
