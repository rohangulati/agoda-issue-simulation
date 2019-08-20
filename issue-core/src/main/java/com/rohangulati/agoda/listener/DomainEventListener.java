package com.rohangulati.agoda.listener;

import com.google.common.eventbus.EventBus;
import com.rohangulati.agoda.event.ProjectQueryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DomainEventListener {

  private final EventBus eventBus;

  @EventListener
  public void onProjectSummaryQueryEvent(ProjectQueryEvent event) {
    eventBus.post(event);
  }
}
