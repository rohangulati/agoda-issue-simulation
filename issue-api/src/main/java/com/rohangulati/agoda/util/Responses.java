package com.rohangulati.agoda.util;

import lombok.Singular;
import lombok.experimental.PackagePrivate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public final class Responses {

  private Responses() {
    // no instance creation
  }

  public static <T> ResponseEntity<T> notFound() {
    return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
  }

  public static <T> ResponseEntity<T> ok(T content) {
    return new ResponseEntity<>(content, HttpStatus.OK);
  }
}
