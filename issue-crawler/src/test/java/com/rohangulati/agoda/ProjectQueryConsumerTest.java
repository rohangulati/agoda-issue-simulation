package com.rohangulati.agoda;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rohangulati.agoda.consumer.ProjectQueryConsumer;
import com.rohangulati.agoda.data.IssuesResponse;
import com.rohangulati.agoda.event.ProjectQueryEvent;
import com.rohangulati.agoda.policy.AlwaysRefreshPolicy;
import com.rohangulati.agoda.policy.RefreshPolicy;
import com.rohangulati.agoda.repository.ThirdPartyIssueRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProjectQueryConsumerTest {

  @Test
  public void shouldRefresh() throws Exception {
    // Given
    BlockingQueue<IssuesResponse> queue = new LinkedBlockingQueue<>();
    EventBus eventBus = new EventBus();
    QueuingListener listener = new QueuingListener(queue);
    eventBus.register(listener);
    ThirdPartyIssueRepository issueRepository = new TestThirdPartyIssueRepository();
    RefreshPolicy refreshPolicy = new AlwaysRefreshPolicy();
    ProjectQueryConsumer consumer =
        new ProjectQueryConsumer(issueRepository, eventBus, refreshPolicy);

    // When
    consumer.onProjectQueryEvent(new ProjectQueryEvent("projectId"));

    // Then
    IssuesResponse actual = queue.take();
    Assert.assertEquals("projectId", actual.getProjectId());
    Assert.assertEquals(Collections.emptyList(), actual.getIssues());
  }

  @Test
  public void shouldNotRefresh() throws Exception {
    // Given
    BlockingQueue<IssuesResponse> queue = new LinkedBlockingQueue<>();
    EventBus eventBus = new EventBus();
    QueuingListener listener = new QueuingListener(queue);
    eventBus.register(listener);
    ThirdPartyIssueRepository issueRepository = new TestThirdPartyIssueRepository();
    RefreshPolicy refreshPolicy = projectId -> false;
    ProjectQueryConsumer consumer =
        new ProjectQueryConsumer(issueRepository, eventBus, refreshPolicy);

    // When
    consumer.onProjectQueryEvent(new ProjectQueryEvent("projectId"));

    // Then
    IssuesResponse actual = queue.poll(10, TimeUnit.SECONDS);
    Assert.assertNull(actual);
  }

  @RequiredArgsConstructor
  private static class QueuingListener {
    private final BlockingQueue<IssuesResponse> queue;

    @Subscribe
    public void onEvent(IssuesResponse response) {
      queue.add(response);
    }
  }

  private static class TestThirdPartyIssueRepository implements ThirdPartyIssueRepository {
    @Override
    public Optional<IssuesResponse> findByProjectId(String projectId) {
      return Optional.of(new IssuesResponse(projectId, Collections.emptyList()));
    }
  }
}
