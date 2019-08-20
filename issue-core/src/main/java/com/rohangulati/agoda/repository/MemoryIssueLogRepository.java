package com.rohangulati.agoda.repository;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.logical.And;
import com.rohangulati.agoda.dao.IssueLog;
import com.rohangulati.agoda.data.ChangeLog;
import com.rohangulati.agoda.data.Issue;
import com.rohangulati.agoda.data.ProjectSummaryRequest;
import com.rohangulati.agoda.data.Week;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * A in memory implementation of a data store using cqengine library. This allows to run sql like queries on in-memory
 * datastructure
 *
 * @see : https://github.com/npgall/cqengine
 */
public class MemoryIssueLogRepository implements IssueLogRepository {

  private interface Indexes {
    Attribute<IssueLog, String> PROJECT_ID = attribute(IssueLog::getProjectId);
    Attribute<IssueLog, String> STATE = attribute(IssueLog::getState);
    Attribute<IssueLog, String> TYPE = attribute(IssueLog::getType);
    Attribute<IssueLog, Week> WEEK = attribute(IssueLog::getWeek);
  }

  private final IndexedCollection<IssueLog> index;

  public MemoryIssueLogRepository() {
    index = new ConcurrentIndexedCollection<>();
    // created index on different field
    index.addIndex(HashIndex.onAttribute(Indexes.PROJECT_ID));
    index.addIndex(HashIndex.onAttribute(Indexes.STATE));
    index.addIndex(HashIndex.onAttribute(Indexes.TYPE));
    index.addIndex(NavigableIndex.onAttribute(Indexes.WEEK));
  }

  @Override
  public List<IssueLog> search(ProjectSummaryRequest request) {
    List<Query<IssueLog>> filters = new ArrayList<>();
    if (!StringUtils.isEmpty(request.getProjectId())) {
      filters.add(equal(Indexes.PROJECT_ID, request.getProjectId()));
    }

    if (!StringUtils.isEmpty(request.getFromWeek())) {
      Week week = Week.of(request.getFromWeek());
      filters.add(greaterThanOrEqualTo(Indexes.WEEK, week));
    }

    if (!StringUtils.isEmpty(request.getToWeek())) {
      Week week = Week.of(request.getToWeek());
      filters.add(lessThanOrEqualTo(Indexes.WEEK, week));
    }
    if (!CollectionUtils.isEmpty(request.getStates())) {
      filters.add(in(Indexes.STATE, request.getStates()));
    }
    if (!CollectionUtils.isEmpty(request.getTypes())) {
      filters.add(in(Indexes.TYPE, request.getTypes()));
    }

    And<IssueLog> query = and(all(IssueLog.class), all(IssueLog.class), filters);
    return index.retrieve(query).stream().collect(Collectors.toList());
  }

  @Override
  public void save(String projectId, Issue issue) {
    List<ChangeLog> logs = issue.getChangeLogs();
    logs.stream()
        .flatMap(
            log -> {
              Week week = Week.of(log.getChangedOn());
              return Stream.of(
                  IssueLog.builder()
                      .issueId(issue.getIssueId())
                      .projectId(projectId)
                      .state(log.getFromState())
                      .type(issue.getType())
                      .week(week)
                      .build(),
                  IssueLog.builder()
                      .issueId(issue.getIssueId())
                      .projectId(projectId)
                      .state(log.getToState())
                      .type(issue.getType())
                      .week(week)
                      .build());
            })
        .filter(issueLog -> !index.contains(issueLog))
        .forEach(index::add);
  }

  @Override
  public Collection<IssueLog> findAll() {
    return Collections.unmodifiableCollection(index);
  }
}
