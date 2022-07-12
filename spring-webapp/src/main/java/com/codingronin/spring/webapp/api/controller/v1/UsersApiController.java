package com.codingronin.spring.webapp.api.controller.v1;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.codingronin.spring.webapp.api.controller.RestApiController;
import com.codingronin.spring.webapp.api.model.http.v1.CreateUserRequest;
import com.codingronin.spring.webapp.api.model.http.v1.CreateUserResponse;
import com.codingronin.spring.webapp.api.model.http.v1.GetUsersResponse;
import com.codingronin.spring.webapp.api.service.UserService;

@RestController("ApiControllerV1")
@RequestMapping("/api/rest/v1/Users")
@Validated
public class UsersApiController implements RestApiController {

  static Logger log = LoggerFactory.getLogger(UsersApiController.class);
  static final String DEFAULT_GET_COUNT = "50";

  @Autowired
  UserService userService;

  @GetMapping
  public ResponseEntity<GetUsersResponse> get(//
      @RequestParam(required = true) int startIndex, //
      @RequestParam(defaultValue = DEFAULT_GET_COUNT) int count//
  ) {
    log.debug("Getting users with startIndex:{}, count:{}", startIndex, count);
    GetUsersResponse resp = new GetUsersResponse();
    resp.setUsers(userService.getUsers(startIndex, count));
    return ResponseEntity.ok(resp);
  }

  @PostMapping
  public ResponseEntity<CreateUserResponse> create(@Valid @RequestBody CreateUserRequest payload) {
    CreateUserResponse resp = new CreateUserResponse();
    resp.setUsers(payload.getUsers());
    return ResponseEntity.ok(resp);
  }

}
