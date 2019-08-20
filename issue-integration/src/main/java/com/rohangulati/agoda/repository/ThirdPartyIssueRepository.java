package com.rohangulati.agoda.repository;

import com.rohangulati.agoda.data.IssuesResponse;

import java.util.Optional;

/** Used for contacting the third party api "/getIssues" */
public interface ThirdPartyIssueRepository {

  /**
   * Returns the issue response for the given {@code projectId}
   *
   * @param projectId
   * @return An {@link Optional} which is empty when response was not received
   */
  Optional<IssuesResponse> findByProjectId(String projectId);
}
