package com.rohangulati.agoda.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectSummary {

  public static ProjectSummary empty(String projectId) {
    return new ProjectSummary(projectId, Collections.emptySet());
  }

  @JsonProperty("project_id")
  private String projectId;

  @JsonProperty("weekly_summaries")
  private Set<ProjectWeekSummary> weeklySummaries;
}
