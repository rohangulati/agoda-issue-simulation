package com.rohangulati.agoda.clock;

import com.rohangulati.agoda.policy.Clock;
import lombok.Data;

@Data
public class TransparentClock implements Clock {
  private long time;

  @Override
  public long currentTime() {
    return time;
  }
}
