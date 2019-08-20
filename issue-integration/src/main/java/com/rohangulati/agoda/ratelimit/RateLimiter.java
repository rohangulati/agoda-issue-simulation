package com.rohangulati.agoda.ratelimit;

/** A rate limiter is rate limit */
public interface RateLimiter {

  /**
   * Call this method before to check if rate limit was exceed or not
   *
   * @return true is ratelimit was not exceeded else false
   */
  boolean tryConsume();
}
