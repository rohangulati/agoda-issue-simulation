package com.rohangulati.agoda.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeekStateSummary implements Comparable<WeekStateSummary> {
  private String state;
  private int count;
  private Set<IssueIdAndType> issues;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WeekStateSummary that = (WeekStateSummary) o;
    return count == that.count
        && Objects.equals(state, that.state)
        && Objects.equals(issues, that.issues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state, count, issues);
  }

  @Override
  public int compareTo(WeekStateSummary o) {
    return this.state.compareTo(o.state);
  }
}
