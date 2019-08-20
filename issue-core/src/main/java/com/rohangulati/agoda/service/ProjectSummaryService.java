package com.rohangulati.agoda.service;

import com.rohangulati.agoda.dao.IssueLog;
import com.rohangulati.agoda.data.*;
import com.rohangulati.agoda.event.ProjectQueryEvent;
import com.rohangulati.agoda.repository.IssueLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service(value = "ProjectSummaryService")
public class ProjectSummaryService {

  @Autowired
  @Qualifier("ProjectSummaryService")
  private ProjectSummaryService self;

  @Autowired private IssueLogRepository issueLogRepository;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  public ProjectSummary getByProjectId(ProjectSummaryRequest request) {
    ProjectSummary result = self.getByProjectIdCached(request);
    applicationEventPublisher.publishEvent(new ProjectQueryEvent(request.getProjectId()));
    return result;
  }

  // Generating project summary is expensive, so do it once and cache it
  @Cacheable(
      cacheManager = "guavaCacheManager",
      value = "issues",
      key = "#request.cacheKey()",
      unless = "#result != null")
  public ProjectSummary getByProjectIdCached(ProjectSummaryRequest request) {
    List<IssueLog> result = issueLogRepository.search(request);
    return buildProjectSummary(request.getProjectId(), result);
  }

  public static ProjectSummary buildProjectSummary(String projectId, List<IssueLog> logs) {
    if (CollectionUtils.isEmpty(logs)) return null;
    // create map by week, issue state and issues
    Map<Week, Map<String, Set<IssueIdAndType>>> grouped = groupByWeekAndState(logs);
    // computes the week summary
    Set<ProjectWeekSummary> perWeekSummary = computeWeeklySummary(grouped);
    return ProjectSummary.builder().projectId(projectId).weeklySummaries(perWeekSummary).build();
  }

  private static Map<Week, Map<String, Set<IssueIdAndType>>> groupByWeekAndState(
      List<IssueLog> logs) {
    Map<Week, Map<String, Set<IssueIdAndType>>> summary = new TreeMap<>();
    for (IssueLog log : logs) {
      Set<IssueIdAndType> issues =
          summary
              .computeIfAbsent(log.getWeek(), k -> new HashMap<>())
              .computeIfAbsent(log.getState(), k -> new HashSet<>());
      issues.add(new IssueIdAndType(log.getIssueId(), log.getType()));
    }
    return summary;
  }

  private static Set<ProjectWeekSummary> computeWeeklySummary(
      Map<Week, Map<String, Set<IssueIdAndType>>> grouped) {
    return grouped.entrySet().stream()
        .map(
            weeklyGroup -> {
              Map<String, Set<IssueIdAndType>> stateToIssues = weeklyGroup.getValue();
              Week week = weeklyGroup.getKey();
              List<WeekStateSummary> weekSummary =
                  stateToIssues.entrySet().stream()
                      .map(
                          entry -> {
                            Set<IssueIdAndType> values = entry.getValue();
                            return WeekStateSummary.builder()
                                .state(entry.getKey())
                                .count(values.size())
                                .issues(values)
                                .build();
                          })
                      .collect(Collectors.toList());
              return ProjectWeekSummary.builder()
                  .week(week.toString())
                  .stateSummaries(weekSummary)
                  .build();
            })
        .collect(Collectors.toSet());
  }
}
