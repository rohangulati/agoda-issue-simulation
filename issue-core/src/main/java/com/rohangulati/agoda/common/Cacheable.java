package com.rohangulati.agoda.common;

/** Describe an object which can be use as a cache key */
public interface Cacheable {

  /**
   * Returns the cache key derived from the values of this object
   *
   * @return the cache key
   */
  String cacheKey();
}
