package com.rohangulati.agoda.listener;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rohangulati.agoda.data.Issue;
import com.rohangulati.agoda.data.IssuesResponse;
import com.rohangulati.agoda.repository.IssueLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class IssueResponseConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(IssueResponseConsumer.class);

  private final EventBus eventBus;

  private final IssueLogRepository issueLogRepository;

  @PostConstruct
  public void registerListener() {
    eventBus.register(this);
  }

  /**
   * Recieves {@link IssuesResponse} from the bus and indexes the value in the data store via {@link
   * IssueLogRepository}
   *
   * @param response
   */
  @Subscribe
  public void onIssueResponse(IssuesResponse response) {
    for (Issue issue : response.getIssues()) {
      LOGGER.info("Indexing [{}] for project [{}]", issue.getIssueId(), response.getProjectId());
      issueLogRepository.save(response.getProjectId(), issue);
      LOGGER.info(
          "Successfully indexed issue [{}] for project [{}]",
          issue.getIssueId(),
          response.getProjectId());
    }
  }
}
