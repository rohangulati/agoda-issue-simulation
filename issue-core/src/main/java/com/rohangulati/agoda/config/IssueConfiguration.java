package com.rohangulati.agoda.config;

import com.google.common.cache.CacheBuilder;
import com.rohangulati.agoda.repository.IssueLogRepository;
import com.rohangulati.agoda.repository.MemoryIssueLogRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class IssueConfiguration {

  @Bean
  public IssueLogRepository issueLogRepository() {
    return new MemoryIssueLogRepository();
  }

  @Bean("guavaCacheManager")
  public CacheManager cacheManager() {
    GuavaCacheManager cacheManager = new GuavaCacheManager("issues");
    CacheBuilder<Object, Object> cacheBuilder =
        CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(2, TimeUnit.MINUTES);
    cacheManager.setCacheBuilder(cacheBuilder);
    return cacheManager;
  }
}
