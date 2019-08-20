package com.rohangulati.agoda.ratelimit;

import com.google.common.base.Preconditions;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

import java.time.Duration;

/**
 * A rate limiter based on Token Bucket Algorithm.
 *
 * e.g.
 * To create a {@link RateLimiter} of 1 request per 1 minute
 * RateLimiter = new Bucket4jRateLimiter(1, Duration.ofMinutes(1));
 *
 * @see - https://github.com/vladimir-bukhtoyarov/bucket4j
 */
public class Bucket4jRateLimiter implements RateLimiter {
  private Bucket bucket;

  public Bucket4jRateLimiter(int rate, Duration duration) {
    Preconditions.checkArgument(rate > 0, "rate cannot be less than or equal to zero");
    // creating rate limit of {rate} requests / {duration} e.g. 1 request / 1 minute
    Bandwidth bandwidth = Bandwidth.simple(rate, duration);
    this.bucket = Bucket4j.builder().addLimit(bandwidth).build();
  }

  @Override
  public boolean tryConsume() {
    return bucket.tryConsume(1);
  }
}
