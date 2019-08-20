package com.rohangulati.agoda.policy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.Duration;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A {@link RefreshPolicy} which decides to refresh data of a give {@code projectId} only when it
 * hasn't seen the given {@code projectId} the last n milliseconds. The idea is similar to a
 * "Sliding Window" where a window of fixed size (n milliseconds in this case) is sliding for ever.
 * This policy decides to refresh only when the projectId is present in the current window.
 */
public class WindowedRefreshPolicy implements RefreshPolicy {
  private final Clock clock;
  private final long backlogTime;
  private final Map<String, Long> values;
  private final Queue<Node> queue;

  public WindowedRefreshPolicy(Duration duration) {
    this(duration, SystemClock.value());
  }

  public WindowedRefreshPolicy(Duration duration, Clock clock) {
    this.clock = clock;
    this.backlogTime = duration.toMillis();
    this.values = new ConcurrentHashMap<>();
    this.queue = new PriorityBlockingQueue<>();
  }

  @Override
  public boolean shouldRefresh(String projectId) {
    long now = clock.currentTime();
    // remove stale values present in the window
    while (!queue.isEmpty() && now - queue.peek().timestamp > backlogTime) {
      Node top = queue.poll();
      if (top == null) break;
      values.computeIfPresent(top.value, (key, count) -> count == 1 ? null : count - 1);
    }
    // check if the projectId is present in the widow
    boolean present = values.containsKey(projectId);
    if (present) return false;
    // if not present add to the window
    queue.add(new Node(projectId, now));
    values.compute(projectId, (key, count) -> count == null ? 1 : count + 1);
    return true;
  }

  @ToString
  @Data
  @AllArgsConstructor
  private static class Node implements Comparable<Node> {
    private String value;
    private long timestamp;

    @Override
    public int compareTo(Node o) {
      return Long.compare(timestamp, o.timestamp);
    }
  }
}
