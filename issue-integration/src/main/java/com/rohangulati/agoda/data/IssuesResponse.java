package com.rohangulati.agoda.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuesResponse {
  @JsonProperty("project_id")
  private String projectId;

  private List<Issue> issues;
}
