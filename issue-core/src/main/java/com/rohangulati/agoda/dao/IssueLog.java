package com.rohangulati.agoda.dao;

import com.rohangulati.agoda.data.Week;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueLog {
  private String projectId;
  private String issueId;
  private String state;
  private String type;
  private Week week;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IssueLog issueLog = (IssueLog) o;
    return projectId.equals(issueLog.projectId) &&
            issueId.equals(issueLog.issueId) &&
            state.equals(issueLog.state) &&
            week.equals(issueLog.week);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectId, issueId, state, week);
  }
}
