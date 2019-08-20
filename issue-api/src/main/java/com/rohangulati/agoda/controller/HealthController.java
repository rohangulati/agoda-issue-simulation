package com.rohangulati.agoda.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.rohangulati.agoda.util.Responses.ok;

@Controller
public class HealthController {

  @RequestMapping(method = RequestMethod.GET, path = "/health/status")
  public ResponseEntity<String> healthStatus() {
    return ok("Up");
  }
}
