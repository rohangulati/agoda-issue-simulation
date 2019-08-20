package com.rohangulati.agoda;

import com.rohangulati.agoda.ratelimit.Bucket4jRateLimiter;
import com.rohangulati.agoda.ratelimit.RateLimiter;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class RateLimiterTest {

  @Test
  public void shouldNotRateLimitAtCapacity() throws Exception {
    // Given
    RateLimiter rateLimiter = new Bucket4jRateLimiter(10, Duration.ofMinutes(1));

    // When
    long actualExecuted = ConcurrentRunner.run(rateLimiter::tryConsume, 10).filter(v -> v).count();

    // Then
    assertEquals(10, actualExecuted);
  }

  @Test
  public void shouldNotRateLimitUnderCapacity() throws Exception {
    // Given
    RateLimiter rateLimiter = new Bucket4jRateLimiter(10, Duration.ofMinutes(1));

    // When
    long actualExecuted = ConcurrentRunner.run(rateLimiter::tryConsume, 7).filter(v -> v).count();

    // Then
    assertEquals(7, actualExecuted);
  }

  @Test
  public void shouldRateLimitOverCapacity() throws Exception {
    // Given
    RateLimiter rateLimiter = new Bucket4jRateLimiter(10, Duration.ofMinutes(1));

    // When
    long actualExecuted = ConcurrentRunner.run(rateLimiter::tryConsume, 100).filter(v -> v).count();

    // Then
    assertEquals(10, actualExecuted);
  }
}
