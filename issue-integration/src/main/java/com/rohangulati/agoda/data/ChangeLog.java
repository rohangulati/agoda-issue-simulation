package com.rohangulati.agoda.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeLog {
  @JsonProperty("changed_on")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mma Z")
  private Date changedOn;

  @JsonProperty("from_state")
  private String fromState;

  @JsonProperty("to_state")
  private String toState;
}
