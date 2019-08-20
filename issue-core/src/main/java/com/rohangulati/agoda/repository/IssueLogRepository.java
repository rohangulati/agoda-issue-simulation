package com.rohangulati.agoda.repository;

import com.rohangulati.agoda.dao.IssueLog;
import com.rohangulati.agoda.data.Issue;
import com.rohangulati.agoda.data.ProjectSummaryRequest;

import java.util.Collection;
import java.util.List;

/** Abstraction for interacting with the data store */
public interface IssueLogRepository {

  List<IssueLog> search(ProjectSummaryRequest query);

  void save(String projectId, Issue issue);

  Collection<IssueLog> findAll();
}
