package com.rohangulati.agoda.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Issue {

  @JsonProperty("issue_id")
  private String issueId;

  private String type;

  @JsonProperty("current_state")
  private String currentState;

  @JsonProperty("changelogs")
  private List<ChangeLog> changeLogs;
}
