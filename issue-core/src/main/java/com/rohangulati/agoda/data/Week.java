package com.rohangulati.agoda.data;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Week implements Comparable<Week> {
  private final int year;
  private final int week;

  public static Week of(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return of(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));
  }

  public static Week of(String s) {
    Preconditions.checkNotNull(s, "input cannot be null");
    Preconditions.checkArgument(!s.isEmpty(), "input cannot be empty");
    String[] parts = s.split("W");
    Preconditions.checkArgument(parts.length == 2, "Invalid format");
    Preconditions.checkArgument(parts[1].length() == 2, "Invalid format of week");
    return of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
  }

  public static Week of(int year, int week) {
    Preconditions.checkArgument(year > 0, "Invalid year", year);
    Preconditions.checkArgument(1 <= week && week <= 53, "Invalid week", week);
    return new Week(year, week);
  }

  @Override
  public int compareTo(Week o) {
    if (this.year != o.year) return Integer.compare(this.year, o.year);
    return Integer.compare(this.week, o.week);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Week week1 = (Week) o;
    return year == week1.year && week == week1.week;
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, week);
  }

  @Override
  public String toString() {
    return String.format("%dW%02d", year, week);
  }
}
