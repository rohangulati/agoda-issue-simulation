package com.rohangulati.agoda;

import com.rohangulati.agoda.dao.IssueLog;
import com.rohangulati.agoda.data.ChangeLog;
import com.rohangulati.agoda.data.Issue;
import com.rohangulati.agoda.data.ProjectSummaryRequest;
import com.rohangulati.agoda.data.Week;
import com.rohangulati.agoda.repository.IssueLogRepository;
import com.rohangulati.agoda.repository.MemoryIssueLogRepository;
import org.assertj.core.util.Sets;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static com.rohangulati.agoda.util.TestFactories.newChangeLog;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class MemoryIssueLogRepositoryTest {

  @Test
  public void shouldSaveIssue() throws Exception {
    // Given
    IssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    List<ChangeLog> changeLogs =
        Arrays.asList(
            newChangeLog("2019-01-02", "s1", "s2"), newChangeLog("2019-01-10", "s2", "s3"));
    Issue issue =
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(changeLogs)
            .build();

    // When
    issueLogRepository.save("projectId", issue);

    // Then
    Set<IssueLog> expected =
        Sets.newLinkedHashSet(
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issue.getIssueId())
                .state("s1")
                .type(issue.getType())
                .week(Week.of(2019, 1))
                .build(),
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issue.getIssueId())
                .state("s2")
                .type(issue.getType())
                .week(Week.of(2019, 1))
                .build(),
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issue.getIssueId())
                .state("s2")
                .type(issue.getType())
                .week(Week.of(2019, 2))
                .build(),
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issue.getIssueId())
                .state("s3")
                .type(issue.getType())
                .week(Week.of(2019, 2))
                .build());
    Collection<IssueLog> actual = issueLogRepository.findAll();
    assertEquals(expected.size(), actual.size());
    assertEquals(expected, new HashSet<>(actual));
  }

  @Test
  public void shouldUpdateIssueLog() throws Exception {
    // Given
    IssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    List<ChangeLog> changeLogs =
        Arrays.asList(
            newChangeLog("2019-01-02", "s1", "s2"), newChangeLog("2019-01-10", "s2", "s3"));
    issueLogRepository.save(
        "projectId",
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(changeLogs)
            .build());

    // When
    Issue issueUpate =
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(singletonList(newChangeLog("2019-01-11", "s3", "s4")))
            .build();
    issueLogRepository.save("projectId", issueUpate);

    // Then
    Set<IssueLog> expected =
        Sets.newLinkedHashSet(
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issueUpate.getIssueId())
                .state("s1")
                .type(issueUpate.getType())
                .week(Week.of(2019, 1))
                .build(),
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issueUpate.getIssueId())
                .state("s2")
                .type(issueUpate.getType())
                .week(Week.of(2019, 1))
                .build(),
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issueUpate.getIssueId())
                .state("s2")
                .type(issueUpate.getType())
                .week(Week.of(2019, 2))
                .build(),
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issueUpate.getIssueId())
                .state("s3")
                .type(issueUpate.getType())
                .week(Week.of(2019, 2))
                .build(),
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issueUpate.getIssueId())
                .state("s4")
                .type(issueUpate.getType())
                .week(Week.of(2019, 2))
                .build());
    Collection<IssueLog> actual = issueLogRepository.findAll();
    assertEquals(expected.size(), actual.size());
    assertEquals(expected, new HashSet<>(actual));
  }

  @Test
  public void shouldAddNewIssue() throws Exception {
    // Given
    IssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    List<ChangeLog> changeLogs = Arrays.asList(newChangeLog("2019-01-02", "s1", "s2"));
    Issue existing =
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(changeLogs)
            .build();
    issueLogRepository.save("projectId", existing);

    // When
    Issue newIssue =
        Issue.builder()
            .issueId("issueId2")
            .type("type1")
            .currentState("s1n")
            .changeLogs(Arrays.asList(newChangeLog("2019-01-18", "s11", "s12")))
            .build();
    issueLogRepository.save("projectId1", newIssue);

    // Then
    Set<IssueLog> expected =
        Sets.newLinkedHashSet(
            IssueLog.builder()
                .projectId("projectId")
                .issueId(existing.getIssueId())
                .state("s1")
                .type(existing.getType())
                .week(Week.of(2019, 1))
                .build(),
            IssueLog.builder()
                .projectId("projectId")
                .issueId(existing.getIssueId())
                .state("s2")
                .type(existing.getType())
                .week(Week.of(2019, 1))
                .build(),
            IssueLog.builder()
                .projectId("projectId1")
                .issueId(newIssue.getIssueId())
                .state("s11")
                .type(newIssue.getType())
                .week(Week.of(2019, 3))
                .build(),
            IssueLog.builder()
                .projectId("projectId1")
                .issueId(newIssue.getIssueId())
                .state("s12")
                .type(newIssue.getType())
                .week(Week.of(2019, 3))
                .build());
    Collection<IssueLog> actual = issueLogRepository.findAll();
    assertEquals(expected.size(), actual.size());
    assertEquals(expected, new HashSet<>(actual));
  }

  @Test
  public void shouldSearchIssueOnEmptyQuery() throws Exception {
    // Given
    IssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    List<ChangeLog> changeLogs = Arrays.asList(newChangeLog("2019-01-02", "s1", "s2"));
    Issue issue1 =
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(changeLogs)
            .build();
    issueLogRepository.save("projectId", issue1);
    Issue issue2 =
        Issue.builder()
            .issueId("issueId2")
            .type("bug")
            .currentState("sn")
            .changeLogs(changeLogs)
            .build();
    issueLogRepository.save("projectId2", issue2);

    // When
    ProjectSummaryRequest request = ProjectSummaryRequest.builder().build();
    List<IssueLog> actual = issueLogRepository.search(request);

    // Then
    Collection<IssueLog> expected = issueLogRepository.findAll();
    assertEquals(expected.size(), actual.size());
    assertEquals(new HashSet<>(expected), new HashSet<>(actual));
  }

  @Test
  public void shouldSearchIssueByProjectId() throws Exception {
    // Given
    IssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    List<ChangeLog> changeLogs = Arrays.asList(newChangeLog("2019-01-02", "s1", "s2"));
    Issue issue1 =
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(changeLogs)
            .build();
    issueLogRepository.save("projectId", issue1);
    Issue issue2 =
        Issue.builder()
            .issueId("issueId2")
            .type("bug")
            .currentState("sn")
            .changeLogs(changeLogs)
            .build();
    issueLogRepository.save("projectId2", issue2);

    // When
    ProjectSummaryRequest request = ProjectSummaryRequest.builder().projectId("projectId").build();
    List<IssueLog> result = issueLogRepository.search(request);

    // Then
    Set<String> actualProjectId =
        result.stream().map(IssueLog::getProjectId).collect(Collectors.toSet());
    Set<String> expectedProjectId = Sets.newLinkedHashSet("projectId");
    assertEquals(2, result.size());
    assertEquals(expectedProjectId, actualProjectId);
  }

  @Test
  public void shouldSearchIssueByStartWeek() throws Exception {
    // Given
    IssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    Issue issue1 =
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-01-02", "s1", "s2")))
            .build();
    issueLogRepository.save("projectId", issue1);
    Issue issue2 =
        Issue.builder()
            .issueId("issueId2")
            .type("bug")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-03-02", "s1", "s2")))
            .build();
    issueLogRepository.save("projectId1", issue2);

    // When
    ProjectSummaryRequest request =
        ProjectSummaryRequest.builder().fromWeek(Week.of(2019, 5).toString()).build();
    List<IssueLog> result = issueLogRepository.search(request);

    // Then
    Set<String> actualProjectId =
        result.stream().map(IssueLog::getProjectId).collect(Collectors.toSet());
    Set<String> expectedProjectId = Sets.newLinkedHashSet("projectId1");
    assertEquals(2, result.size());
    assertEquals(expectedProjectId, actualProjectId);
  }

  @Test
  public void shouldSearchIssueByEndWeek() throws Exception {
    // Given
    IssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    Issue issue1 =
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-01-02", "s1", "s2")))
            .build();
    issueLogRepository.save("projectId", issue1);
    Issue issue2 =
        Issue.builder()
            .issueId("issueId2")
            .type("bug")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-03-02", "s1", "s2")))
            .build();
    issueLogRepository.save("projectId1", issue2);

    // When
    ProjectSummaryRequest request =
        ProjectSummaryRequest.builder().toWeek(Week.of(2019, 5).toString()).build();
    List<IssueLog> result = issueLogRepository.search(request);

    // Then
    Set<String> actualProjectId =
        result.stream().map(IssueLog::getProjectId).collect(Collectors.toSet());
    Set<String> expectedProjectId = Sets.newLinkedHashSet("projectId");
    assertEquals(2, result.size());
    assertEquals(expectedProjectId, actualProjectId);
  }

  @Test
  public void shouldSearchIssueByType() throws Exception {
    // Given
    IssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    Issue issue1 =
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-01-02", "s1", "s2")))
            .build();
    issueLogRepository.save("projectId", issue1);
    Issue issue2 =
        Issue.builder()
            .issueId("issueId2")
            .type("task")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-03-02", "s1", "s2")))
            .build();
    issueLogRepository.save("projectId1", issue2);

    // When
    ProjectSummaryRequest request =
        ProjectSummaryRequest.builder().types(Collections.singleton("task")).build();
    List<IssueLog> result = issueLogRepository.search(request);

    // Then
    Set<String> actualProjectId =
        result.stream().map(IssueLog::getProjectId).collect(Collectors.toSet());
    Set<String> expectedProjectId = Sets.newLinkedHashSet("projectId1");
    assertEquals(2, result.size());
    assertEquals(expectedProjectId, actualProjectId);
  }

  @Test
  public void shouldSearchIssueByState() throws Exception {
    // Given
    IssueLogRepository issueLogRepository = new MemoryIssueLogRepository();
    Issue issue1 =
        Issue.builder()
            .issueId("issueId")
            .type("bug")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-01-02", "s1", "s2")))
            .build();
    issueLogRepository.save("projectId", issue1);
    Issue issue2 =
        Issue.builder()
            .issueId("issueId2")
            .type("task")
            .currentState("sn")
            .changeLogs(Arrays.asList(newChangeLog("2019-01-12", "s1", "s2")))
            .build();
    issueLogRepository.save("projectId1", issue2);

    // When
    ProjectSummaryRequest request =
        ProjectSummaryRequest.builder().states(Collections.singleton("s2")).build();
    List<IssueLog> actual = issueLogRepository.search(request);

    // Then
    Set<IssueLog> expected =
        Sets.newLinkedHashSet(
            IssueLog.builder()
                .projectId("projectId")
                .issueId(issue1.getIssueId())
                .state("s2")
                .type(issue1.getType())
                .week(Week.of(2019, 1))
                .build(),
            IssueLog.builder()
                .projectId("projectId1")
                .issueId(issue2.getIssueId())
                .state("s2")
                .type(issue2.getType())
                .week(Week.of(2019, 2))
                .build());
    assertEquals(expected.size(), actual.size());
    assertEquals(expected, new HashSet<>(actual));
  }
}
