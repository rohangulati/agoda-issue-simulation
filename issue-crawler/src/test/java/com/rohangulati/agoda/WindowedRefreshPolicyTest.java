package com.rohangulati.agoda;

import com.rohangulati.agoda.clock.TransparentClock;
import com.rohangulati.agoda.policy.RefreshPolicy;
import com.rohangulati.agoda.policy.WindowedRefreshPolicy;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;

public class WindowedRefreshPolicyTest {

  @Test
  public void shouldRefresh() throws Exception {
    // Given
    TransparentClock clock = new TransparentClock();
    RefreshPolicy refreshPolicy = new WindowedRefreshPolicy(Duration.ofMinutes(1), clock);
    clock.setTime(0);
    refreshPolicy.shouldRefresh("projectId1");
    refreshPolicy.shouldRefresh("projectId2");

    // When
    clock.setTime(Duration.ofMinutes(1).toMillis() + 1);
    boolean actual = refreshPolicy.shouldRefresh("projectId1");

    // Then
    Assert.assertTrue(actual);
  }

  @Test
  public void shouldNotRefresh() throws Exception {
    // Given
    TransparentClock clock = new TransparentClock();
    RefreshPolicy refreshPolicy = new WindowedRefreshPolicy(Duration.ofMinutes(1), clock);
    clock.setTime(0);
    refreshPolicy.shouldRefresh("projectId1");
    refreshPolicy.shouldRefresh("projectId2");

    // When
    clock.setTime(Duration.ofMinutes(1).toMillis());
    boolean actualBoundary = refreshPolicy.shouldRefresh("projectId1");

    clock.setTime(Duration.ofMinutes(1).toMillis() - 1);
    boolean actual = refreshPolicy.shouldRefresh("projectId1");

    // Then
    Assert.assertFalse(actualBoundary);
    Assert.assertFalse(actual);
  }
}
