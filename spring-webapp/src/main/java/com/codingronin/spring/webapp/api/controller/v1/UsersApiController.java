package com.codingronin.spring.webapp.api.controller.v1;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codingronin.spring.webapp.api.controller.RestApiController;
import com.codingronin.spring.webapp.api.model.http.v1.CreateUserRequest;
import com.codingronin.spring.webapp.api.model.http.v1.CreateUserResponse;
import com.codingronin.spring.webapp.api.model.http.v1.GetUsersResponse;

@RestController("ApiControllerV1")
@RequestMapping("/api/v1/Users")
@Validated
public class UsersApiController implements RestApiController {

  @GetMapping
  public ResponseEntity<GetUsersResponse> get() {
    GetUsersResponse resp = new GetUsersResponse();
    return ResponseEntity.ok(resp);
  }

  @PostMapping
  public ResponseEntity<CreateUserResponse> create(@Valid @RequestBody CreateUserRequest payload) {
    CreateUserResponse resp = new CreateUserResponse();
    resp.setUsers(payload.getUsers());
    return ResponseEntity.ok(resp);
  }

}
