package com.rohangulati.agoda.controller;

import com.rohangulati.agoda.data.ProjectSummary;
import com.rohangulati.agoda.data.ProjectSummaryRequest;
import com.rohangulati.agoda.service.ProjectSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

import static com.rohangulati.agoda.util.Responses.ok;

@Controller
public class ProjectReportController {

  @Autowired private ProjectSummaryService projectSummaryService;

  @RequestMapping(method = RequestMethod.POST, path = "/getWeeklySummary")
  public ResponseEntity<ProjectSummary> getProjectSummary(
      @Valid @RequestBody ProjectSummaryRequest request) {
    ProjectSummary response = projectSummaryService.getByProjectId(request);
    if (response == null) response = ProjectSummary.empty(request.getProjectId());
    return ok(response);
  }
}
