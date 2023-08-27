package com.codingronin.spring.webapp.api.controller.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codingronin.spring.webapp.api.model.http.v1.ApiBaseResponse;

@RestController
@RequestMapping("/api/rest/v1/HealthCheck")
@Validated
public class HealthCheckController {

  @GetMapping("/ping")
  public ResponseEntity<ApiBaseResponse> ping() {
    ApiBaseResponse resp = new ApiBaseResponse();
    resp.setStatusMessage("Server is up");
    return ResponseEntity.ok(resp);
  }


}
