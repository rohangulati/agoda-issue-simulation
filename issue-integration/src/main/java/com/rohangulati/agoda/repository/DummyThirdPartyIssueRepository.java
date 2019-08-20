package com.rohangulati.agoda.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohangulati.agoda.data.IssuesResponse;

import java.io.IOException;
import java.util.Optional;

public class DummyThirdPartyIssueRepository implements ThirdPartyIssueRepository {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Optional<IssuesResponse> findByProjectId(String projectId) {
    String s =
        "{\"project_id\":\"project1\",\"issues\":[{\"issue_id\":\"issue1\",\"type\":\"bug\",\"current_state\":\"open\",\"changelogs\":[{\"changed_on\":\"2019-01-01 12:00pm UTC\",\"from_state\":\"open\",\"to_state\":\"in_progress\"},{\"changed_on\":\"2019-01-03 12:00pm UTC\",\"from_state\":\"in_progress\",\"to_state\":\"testing\"},{\"changed_on\":\"2019-01-21 12:00pm UTC\",\"from_state\":\"testing\",\"to_state\":\"deploy\"}]}]}";
    try {
      return Optional.ofNullable(objectMapper.readValue(s, IssuesResponse.class))
          .map(
              issuesResponse -> {
                issuesResponse.setProjectId(projectId);
                return issuesResponse;
              });
    } catch (IOException e) {
    }
    return Optional.empty();
  }
}
