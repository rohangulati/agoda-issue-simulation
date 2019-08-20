package com.rohangulati.agoda.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectWeekSummary implements Comparable<ProjectWeekSummary> {
  private String week;

  @JsonProperty("state_summaries")
  private List<WeekStateSummary> stateSummaries;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProjectWeekSummary that = (ProjectWeekSummary) o;
    return week.equals(that.week);
  }

  @Override
  public int hashCode() {
    return Objects.hash(week);
  }

  @Override
  public int compareTo(ProjectWeekSummary o) {
    Week w1 = Week.of(week);
    Week w2 = Week.of(o.week);
    return w1.compareTo(w2);
  }
}
