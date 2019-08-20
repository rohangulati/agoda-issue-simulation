package com.rohangulati.agoda.config;

import com.rohangulati.agoda.ratelimit.Bucket4jRateLimiter;
import com.rohangulati.agoda.ratelimit.RateLimiter;
import com.rohangulati.agoda.repository.DummyThirdPartyIssueRepository;
import com.rohangulati.agoda.repository.RateLimitingIssueRepository;
import com.rohangulati.agoda.repository.ThirdPartyIssueRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class IssueIntegrationConfiguration {

  @Bean
  public ThirdPartyIssueRepository thirdPartyIssueRepository(
      RateLimiter rateLimiter, ThirdPartyIssueRepository upstreamIssueRepository) {
    return new RateLimitingIssueRepository(rateLimiter, upstreamIssueRepository);
  }

  @Bean
  public ThirdPartyIssueRepository upstreamIssueRepository() {
    return new DummyThirdPartyIssueRepository();
  }

  @Bean
  public RateLimiter rateLimiter() {
    // rate limit of 1 request / 1 minute
    return new Bucket4jRateLimiter(1, Duration.ofMinutes(1));
  }
}
