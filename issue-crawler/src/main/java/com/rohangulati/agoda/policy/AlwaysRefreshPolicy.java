package com.rohangulati.agoda.policy;

/** A {@link RefreshPolicy} which always decides to refresh */
public class AlwaysRefreshPolicy implements RefreshPolicy {
  @Override
  public boolean shouldRefresh(String projectId) {
    return true;
  }
}
