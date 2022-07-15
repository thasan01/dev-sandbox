package com.codingronin.spring.webapp.api.controller.v1;

import static com.codingronin.spring.webapp.api.util.CollectionsUtil.nullSafe;
import java.util.List;
import java.util.stream.Collectors;
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
import com.codingronin.spring.webapp.api.model.v1.User;
import com.codingronin.spring.webapp.api.service.UserService;

@RestController("ApiControllerV1")
@RequestMapping("/api/rest/v1/Users")
@Validated
public class UsersApiController implements RestApiController {

  static Logger log = LoggerFactory.getLogger(UsersApiController.class);
  static final String DEFAULT_GET_SIZE = "50";

  @Autowired
  UserService userService;

  @GetMapping
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
  public ResponseEntity<CreateUserResponse> create(@Valid @RequestBody CreateUserRequest payload) {

    List<User> users = nullSafe(payload.getUsers()).stream().map(elem -> {
      User user = new User();
      user.setUserName(elem.getUserName());
      user.setEmail(elem.getEmail());
      return user;
    }).collect(Collectors.toList());

    CreateUserResponse resp = new CreateUserResponse();
    resp.setUsers(userService.createUsers(users));
    return ResponseEntity.ok(resp);
  }


}
