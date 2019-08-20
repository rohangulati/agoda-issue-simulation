package com.rohangulati.agoda.policy;

/**
 * A refresh policy is used to check for a given {@code projectId} should new data be fetched The
 * intent of a {@link RefreshPolicy} is to make sure that one type of request in not consuming all
 * the bandwidth of our downstream throttled service. This allows to efficiently use the limited
 * bandwidth to query data for different projectId's when the query pattern is skewed and prevents
 * starvation of the minority query.
 */
public interface RefreshPolicy {

  /**
   * Decides of the data for the given {@code projectId} should be refreshed or not
   *
   * @param projectId
   * @return the refresh decision, true when the data should be refreshed, else false
   */
  boolean shouldRefresh(String projectId);
}
