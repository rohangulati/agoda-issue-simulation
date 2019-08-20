package com.rohangulati.agoda.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueIdAndType {
  @JsonProperty("issue_id")
  private String issueId;
  private String type;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IssueIdAndType that = (IssueIdAndType) o;
    return Objects.equals(issueId, that.issueId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(issueId);
  }
}
