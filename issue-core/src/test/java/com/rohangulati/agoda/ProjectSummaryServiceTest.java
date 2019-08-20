package com.rohangulati.agoda;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rohangulati.agoda.dao.IssueLog;
import com.rohangulati.agoda.data.*;
import com.rohangulati.agoda.event.ProjectQueryEvent;
import com.rohangulati.agoda.repository.IssueLogRepository;
import com.rohangulati.agoda.service.ProjectSummaryService;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.rohangulati.agoda.util.TestFactories.newChangeLog;
import static org.junit.Assert.*;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectSummaryServiceTest {
  @Autowired private ProjectSummaryService projectSummaryService;

  @Autowired private EventBus eventBus;

  @Autowired private IssueLogRepository issueLogRepository;

  @Test
  public void shouldGenerateEmptyProjectSummary() throws Exception {
    // When
    assertNull(ProjectSummaryService.buildProjectSummary("projectId", null));
    assertNull(ProjectSummaryService.buildProjectSummary("projectId", Collections.emptyList()));
  }

  @Test
  public void shouldGenerateProjectSymmary() throws Exception {
    // Given
    List<IssueLog> logs =
        Arrays.asList(
            IssueLog.builder()
                .projectId("projectId")
                .issueId("issue1")
                .state("s2")
                .type("t1")
                .week(Week.of(2019, 1))
                .build(),
            IssueLog.builder()
                .projectId("projectId")
                .issueId("issue2")
                .state("s3")
                .type("t2")
                .week(Week.of(2019, 2))
                .build());

    // When
    ProjectSummary actual = ProjectSummaryService.buildProjectSummary("projectId", logs);

    // Then
    assertNotNull(actual);
    assertEquals("projectId", actual.getProjectId());
    Set<ProjectWeekSummary> expectedSummaries = Sets.newTreeSet();
    expectedSummaries.add(
        ProjectWeekSummary.builder()
            .week(Week.of(2019, 1).toString())
            .stateSummaries(
                Arrays.asList(
                    WeekStateSummary.builder()
                        .count(1)
                        .state("s2")
                        .issues(Sets.newHashSet(new IssueIdAndType("issue1", "t1")))
                        .build()))
            .build());
    expectedSummaries.add(
        ProjectWeekSummary.builder()
            .week(Week.of(2019, 2).toString())
            .stateSummaries(
                Arrays.asList(
                    WeekStateSummary.builder()
                        .count(1)
                        .state("s3")
                        .issues(Sets.newHashSet(new IssueIdAndType("issue2", "t2")))
                        .build()))
            .build());

    Map<String, List<WeekStateSummary>> expectedMap =
        expectedSummaries.stream()
            .collect(
                Collectors.toMap(
                    ProjectWeekSummary::getWeek, ProjectWeekSummary::getStateSummaries));
    Map<String, List<WeekStateSummary>> actualMap =
        actual.getWeeklySummaries().stream()
            .collect(
                Collectors.toMap(
                    ProjectWeekSummary::getWeek, ProjectWeekSummary::getStateSummaries));

    assertEquals(expectedMap.size(), actualMap.size());
    assertEquals(expectedMap.keySet(), actualMap.keySet());
    for (String week : expectedMap.keySet()) {
      List<WeekStateSummary> expectedWeek = expectedMap.get(week);
      List<WeekStateSummary> actualWeek = actualMap.get(week);
      assertNotNull(actualWeek);
      assertEquals(expectedWeek, actualWeek);
    }
  }

  @Test
  public void shouldPublishProjectSummaryEvent() throws Exception {
    // Given
    BlockingQueue<ProjectQueryEvent> queue = new LinkedBlockingDeque<>();
    TestListener eventListener = new TestListener(queue);
    eventBus.register(eventListener);

    // When
    projectSummaryService.getByProjectId(
        ProjectSummaryRequest.builder().projectId("projectId").build());

    // Then
    ProjectQueryEvent actualEvent = queue.poll(10, TimeUnit.MINUTES);
    assertNotNull(actualEvent);
    assertEquals("projectId", actualEvent.getProjectId());
  }

  @Test
  public void shouldGetProjectSummary() throws Exception {
    Issue issue1 =
        Issue.builder()
            .issueId("issue1")
            .type("t1")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-01-01", "s1", "s2")))
            .build();
    Issue issue2 =
        Issue.builder()
            .issueId("issue2")
            .type("t2")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-01-12", "s2", "s3")))
            .build();
    issueLogRepository.save("projectId", issue1);
    issueLogRepository.save("projectId", issue2);

    // When
    ProjectSummary actual =
        projectSummaryService.getByProjectId(
            ProjectSummaryRequest.builder().projectId("projectId").build());

    // Then
    assertNotNull(actual);
    assertEquals("projectId", actual.getProjectId());
    Set<ProjectWeekSummary> expectedSummaries = Sets.newTreeSet();
    expectedSummaries.add(
        ProjectWeekSummary.builder()
            .week(Week.of(2019, 1).toString())
            .stateSummaries(
                Arrays.asList(
                    WeekStateSummary.builder()
                        .count(1)
                        .state("s1")
                        .issues(Sets.newHashSet(new IssueIdAndType("issue1", "t1")))
                        .build(),
                    WeekStateSummary.builder()
                        .count(1)
                        .state("s2")
                        .issues(Sets.newHashSet(new IssueIdAndType("issue1", "t1")))
                        .build()))
            .build());
    expectedSummaries.add(
        ProjectWeekSummary.builder()
            .week(Week.of(2019, 2).toString())
            .stateSummaries(
                Arrays.asList(
                    WeekStateSummary.builder()
                        .count(1)
                        .state("s2")
                        .issues(Sets.newHashSet(new IssueIdAndType("issue2", "t2")))
                        .build(),
                    WeekStateSummary.builder()
                        .count(1)
                        .state("s3")
                        .issues(Sets.newHashSet(new IssueIdAndType("issue2", "t2")))
                        .build()))
            .build());

    Map<String, List<WeekStateSummary>> expectedMap =
        expectedSummaries.stream()
            .collect(
                Collectors.toMap(
                    ProjectWeekSummary::getWeek, ProjectWeekSummary::getStateSummaries));
    Map<String, List<WeekStateSummary>> actualMap =
        actual.getWeeklySummaries().stream()
            .collect(
                Collectors.toMap(
                    ProjectWeekSummary::getWeek, ProjectWeekSummary::getStateSummaries));

    assertEquals(expectedMap.size(), actualMap.size());
    assertEquals(expectedMap.keySet(), actualMap.keySet());
    for (String week : expectedMap.keySet()) {
      List<WeekStateSummary> expectedWeek = expectedMap.get(week);
      List<WeekStateSummary> actualWeek = actualMap.get(week);
      assertNotNull(actualWeek);
      Collections.sort(expectedWeek);
      Collections.sort(actualWeek);
      assertEquals(expectedWeek, actualWeek);
    }
  }

  @RequiredArgsConstructor
  private static class TestListener {
    private final BlockingQueue<ProjectQueryEvent> queue;

    @Subscribe
    public void onEvent(ProjectQueryEvent event) {
      queue.add(event);
    }
  }
}
