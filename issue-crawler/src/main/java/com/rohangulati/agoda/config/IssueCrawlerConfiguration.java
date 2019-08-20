package com.rohangulati.agoda.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.rohangulati.agoda.policy.RefreshPolicy;
import com.rohangulati.agoda.policy.WindowedRefreshPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.Executors;

@Configuration
public class IssueCrawlerConfiguration {

  @Bean
  public EventBus eventBus() {
    return new AsyncEventBus("issue-query-queue", Executors.newFixedThreadPool(10));
  }

  @Bean
  public RefreshPolicy refreshPolicy() {
    return new WindowedRefreshPolicy(Duration.ofMinutes(1));
  }
}
