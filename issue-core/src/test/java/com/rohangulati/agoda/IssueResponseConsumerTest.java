package com.rohangulati.agoda;

import com.google.common.eventbus.EventBus;
import com.rohangulati.agoda.dao.IssueLog;
import com.rohangulati.agoda.data.Issue;
import com.rohangulati.agoda.data.IssuesResponse;
import com.rohangulati.agoda.listener.IssueResponseConsumer;
import com.rohangulati.agoda.repository.MemoryIssueLogRepository;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.rohangulati.agoda.util.TestFactories.newChangeLog;
import static org.junit.Assert.assertEquals;

public class IssueResponseConsumerTest {

  @Test
  public void testIssueResponseSaved() throws Exception {
    // Given
    EventBus eventBus = new EventBus();
    MemoryIssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    IssueResponseConsumer consumer = new IssueResponseConsumer(eventBus, issueLogRepository);

    // When
    List<Issue> issues =
        Arrays.asList(
            Issue.builder()
                .issueId("issueId")
                .type("bug")
                .currentState("sn")
                .changeLogs(Arrays.asList(newChangeLog("2019-01-02", "s1", "s2")))
                .build(),
            Issue.builder()
                .issueId("issueId2")
                .type("task")
                .currentState("sn")
                .changeLogs(Arrays.asList(newChangeLog("2019-01-12", "s1", "s2")))
                .build());
    IssuesResponse response = new IssuesResponse("projectId", issues);
    consumer.onIssueResponse(response);

    // Then
    Collection<IssueLog> result = issueLogRepository.findAll();
    assertEquals(4, result.size());
  }
}
