package com.codingronin.spring.webapp.api.controller.v1;

import static com.codingronin.spring.webapp.api.util.CollectionsUtil.nullSafe;
import static com.codingronin.spring.webapp.filter.TransactionFilter.CLIENT_RESPONSE_ID_KEY;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.codingronin.spring.webapp.api.controller.RestApiController;
import com.codingronin.spring.webapp.api.model.http.v1.CreateUser;
import com.codingronin.spring.webapp.api.model.http.v1.CreateUserRequest;
import com.codingronin.spring.webapp.api.model.http.v1.CreateUserResponse;
import com.codingronin.spring.webapp.api.model.http.v1.DeleteUserRequest;
import com.codingronin.spring.webapp.api.model.http.v1.DeleteUserResponse;
import com.codingronin.spring.webapp.api.model.http.v1.GetUsersResponse;
import com.codingronin.spring.webapp.api.model.http.v1.UpdateUserAttributesRequest;
import com.codingronin.spring.webapp.api.model.http.v1.UpdateUserAttributesResponse;
import com.codingronin.spring.webapp.api.model.v1.User;
import com.codingronin.spring.webapp.api.service.UserService;

@RestController("UsersApiControllerV1")
@RequestMapping("/api/rest/v1/Users")
@Validated
public class UsersApiController extends RestApiController {

  static Logger log = LoggerFactory.getLogger(UsersApiController.class);

  @Autowired
  UserService userService;

  @GetMapping
  @PreAuthorize("hasAnyAuthority('*::*::*', 'API::*::VIEW', 'API::USERS::VIEW')")
  public ResponseEntity<GetUsersResponse> get(//
      @RequestParam(required = true) int page, //
      @RequestParam(defaultValue = DEFAULT_GET_SIZE) int size//
  ) {
    log.debug("Getting users with startIndex:{}, count:{}", page, size);
    GetUsersResponse resp = new GetUsersResponse();
    resp.setUsers(userService.getUsers(page, size));
    return ResponseEntity.ok(resp);
  }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('*::*::*', 'API::*::CREATE', 'API::USERS::CREATE')")
  public ResponseEntity<CreateUserResponse> create(@Valid @RequestBody CreateUserRequest payload) {

    CreateUserResponse resp = new CreateUserResponse();
    resp.setUsers(userService.createUsers((List<CreateUser>) nullSafe(payload.getUsers())));
    return ResponseEntity.ok(resp);
  }

  @DeleteMapping
  @PreAuthorize("hasAnyAuthority('*::*::*', 'API::*::DELETE', 'API::USERS::DELETE')")
  public ResponseEntity<DeleteUserResponse> create(@Valid @RequestBody DeleteUserRequest payload) {
    userService.deleteUsers(payload.getUserNames());
    DeleteUserResponse resp = new DeleteUserResponse();
    resp.setUserNames(payload.getUserNames());
    return ResponseEntity.ok(resp);
  }

  @PatchMapping
  @PreAuthorize("hasAnyAuthority('*::*::*', 'API::*::UPDATE', 'API::USERS::UPDATE')")
  public ResponseEntity<UpdateUserAttributesResponse> updateAttributes(
      @RequestAttribute(name = CLIENT_RESPONSE_ID_KEY) String responseId,
      @Valid @RequestBody UpdateUserAttributesRequest payload) {
    List<User> users = userService.updateAttributes(responseId, payload.getUsers());
    UpdateUserAttributesResponse resp = new UpdateUserAttributesResponse();
    resp.setUsers(users);
    return ResponseEntity.ok(resp);
  }

}
