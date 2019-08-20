package com.rohangulati.agoda.repository;

import com.rohangulati.agoda.data.IssuesResponse;
import com.rohangulati.agoda.ratelimit.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RateLimitingIssueRepository implements ThirdPartyIssueRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitingIssueRepository.class);

  private final RateLimiter rateLimiter;
  private final ThirdPartyIssueRepository actual;

  public RateLimitingIssueRepository(RateLimiter rateLimiter, ThirdPartyIssueRepository actual) {
    this.rateLimiter = rateLimiter;
    this.actual = actual;
  }

  @Override
  public Optional<IssuesResponse> findByProjectId(String projectId) {
    if (rateLimiter.tryConsume()) {
      LOGGER.info("Calling issues api for project [{}]", projectId);
      return actual.findByProjectId(projectId);
    }
    LOGGER.info(
        "Rate limit exceeded for issues api. Skipping call to issues api for project [{}]",
        projectId);
    return Optional.empty();
  }
}
