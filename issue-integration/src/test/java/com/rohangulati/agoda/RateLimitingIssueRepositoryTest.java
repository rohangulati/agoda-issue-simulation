package com.rohangulati.agoda;

import com.rohangulati.agoda.data.IssuesResponse;
import com.rohangulati.agoda.ratelimit.Bucket4jRateLimiter;
import com.rohangulati.agoda.ratelimit.RateLimiter;
import com.rohangulati.agoda.repository.RateLimitingIssueRepository;
import com.rohangulati.agoda.repository.ThirdPartyIssueRepository;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class RateLimitingIssueRepositoryTest {

  @Test
  public void shouldNotRatelimit() throws Exception {
    // Given
    RateLimiter rateLimiter = new Bucket4jRateLimiter(10, Duration.ofMinutes(1));
    ThirdPartyIssueRepository repository =
        new RateLimitingIssueRepository(rateLimiter, new TestThirdPartyIssueRepository());

    // When
    long actualExecuted =
        ConcurrentRunner.run(() -> repository.findByProjectId("projectId"), 10)
            .filter(Objects::nonNull)
            .filter(Optional::isPresent)
            .count();

    // Then
    Assert.assertEquals(10, actualExecuted);
  }

  @Test
  public void shouldRateLimit() throws Exception {
    // Given
    RateLimiter rateLimiter = new Bucket4jRateLimiter(10, Duration.ofMinutes(1));
    ThirdPartyIssueRepository repository =
        new RateLimitingIssueRepository(rateLimiter, new TestThirdPartyIssueRepository());

    // When
    long actualExecuted =
        ConcurrentRunner.run(() -> repository.findByProjectId("projectId"), 100)
            .filter(Objects::nonNull)
            .filter(Optional::isPresent)
            .count();

    // Then
    Assert.assertEquals(10, actualExecuted);
  }

  private static class TestThirdPartyIssueRepository implements ThirdPartyIssueRepository {
    @Override
    public Optional<IssuesResponse> findByProjectId(String projectId) {
      return Optional.of(new IssuesResponse(projectId, Collections.emptyList()));
    }
  }
}
