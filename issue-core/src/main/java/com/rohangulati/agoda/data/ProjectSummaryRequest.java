package com.rohangulati.agoda.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rohangulati.agoda.common.Cacheable;
import com.rohangulati.agoda.validation.Week;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSummaryRequest implements Cacheable {

  @NotBlank(message = "project id cannot be blank")
  @JsonProperty("project_id")
  private String projectId;

  @Week(message = "Invalid from week format")
  @JsonProperty("from_week")
  private String fromWeek;

  @Week(message = "Invalid to week format")
  @JsonProperty("to_week")
  private String toWeek;

  private Set<String> types;

  private Set<String> states;

  @Override
  public String cacheKey() {
    String typeHash = "";
    if (!CollectionUtils.isEmpty(types)) {
      typeHash = types.stream().sorted().collect(Collectors.joining(","));
    }

    String statesHash = "";
    if (!CollectionUtils.isEmpty(states)) {
      statesHash = states.stream().sorted().collect(Collectors.joining(","));
    }

    return String.valueOf(
        new StringBuilder(projectId)
            .append("&fw=").append(fromWeek)
            .append("&tw=").append(toWeek)
            .append("&t=").append(typeHash)
            .append("&s=").append(statesHash)
            .hashCode());
  }
}
