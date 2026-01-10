package com.codingronin.spring.webapp.scim.controller.v2;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.bettercloud.scim2.common.BaseScimResource;
import com.bettercloud.scim2.common.messages.ErrorResponse;
import com.bettercloud.scim2.common.messages.ListResponse;
import com.bettercloud.scim2.common.types.UserResource;
import com.bettercloud.scim2.server.annotation.ScimResource;
import com.codingronin.spring.webapp.api.model.v1.User;
import com.codingronin.spring.webapp.api.service.UserService;
import com.codingronin.spring.webapp.scim.dto.v2.ScimUserMapper;

@RestController
@ScimResource(description = "Access User Resources", name = "User", schema = UserResource.class)
@RequestMapping("/api/scim/v2/Users")
public class ScimUsersController {

  @Autowired
  ScimUserMapper scimUserMapper;

  @Autowired
  UserService userService;

  @GetMapping
  @ResponseBody
  public ListResponse<UserResource> getUsers() {

    int totalResults = 0;
    int startIndex = 0;
    Integer itemsPerPage = 10;
    List<UserResource> resources =
        scimUserMapper.toScim(userService.getUsers(startIndex, itemsPerPage));

    ListResponse<UserResource> resp =
        new ListResponse<>(totalResults, resources, startIndex, itemsPerPage);

    resp.setId(UUID.randomUUID().toString());
    return resp;
  }

  @GetMapping("/{id}")
  @ResponseBody
  BaseScimResource getUser(@PathVariable(required = true) Integer id,
      @RequestParam(required = false) String attributes) {

    User user = userService.getUser(id);

    if (user == null) {
      ErrorResponse errResp = new ErrorResponse(404);
      errResp.setDetail("User not found with id: " + id);
      return errResp;
    }

    return scimUserMapper.toScim(user);
  }

}
