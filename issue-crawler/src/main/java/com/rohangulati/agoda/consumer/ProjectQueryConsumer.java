package com.rohangulati.agoda.consumer;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rohangulati.agoda.data.IssuesResponse;
import com.rohangulati.agoda.event.ProjectQueryEvent;
import com.rohangulati.agoda.policy.RefreshPolicy;
import com.rohangulati.agoda.repository.ThirdPartyIssueRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectQueryConsumer {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProjectQueryConsumer.class);

  private final ThirdPartyIssueRepository thirdPartyIssueRepository;

  private final EventBus eventBus;

  private final RefreshPolicy refreshPolicy;

  @PostConstruct
  public void register() {
    this.eventBus.register(this);
  }

  /**
   * Recieves the {@link ProjectQueryEvent} and acts to refresh the data based on the {@link
   * RefreshPolicy}. If new data in published backed to the event bus for asynchronous consumption
   *
   * @param event
   */
  @Subscribe
  public void onProjectQueryEvent(ProjectQueryEvent event) {
    boolean decision = refreshPolicy.shouldRefresh(event.getProjectId());
    LOGGER.info("Refresh decision for event {} is [{}]", event, decision);
    if (decision) {
      Optional<IssuesResponse> result =
          thirdPartyIssueRepository.findByProjectId(event.getProjectId());
      result.ifPresent(eventBus::post);
    }
  }
}
