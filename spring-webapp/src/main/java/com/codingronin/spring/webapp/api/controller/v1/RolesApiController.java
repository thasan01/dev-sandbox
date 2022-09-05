package com.codingronin.spring.webapp.api.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.codingronin.spring.webapp.api.controller.RestApiController;
import com.codingronin.spring.webapp.api.model.http.v1.GetRolesResponse;
import com.codingronin.spring.webapp.api.model.http.v1.GetSingleRoleResponse;
import com.codingronin.spring.webapp.api.service.RoleService;

@RestController("RolesApiControllerV1")
@RequestMapping("/api/rest/v1/Roles")
public class RolesApiController extends RestApiController {
  static Logger log = LoggerFactory.getLogger(RolesApiController.class);

  @Autowired
  RoleService roleService;

  @GetMapping
  @PreAuthorize("hasAnyAuthority('*::*::*', 'API::*::VIEW', 'API::ROLES::VIEW')")
  public ResponseEntity<GetRolesResponse> get(//
      @RequestParam(required = true) int page, //
      @RequestParam(defaultValue = DEFAULT_GET_SIZE) int size//
  ) {
    GetRolesResponse resp = new GetRolesResponse();
    resp.setRoles(roleService.getRoles(page, size));
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/{roleName}")
  @PreAuthorize("hasAnyAuthority('*::*::*', 'API::*::VIEW', 'API::ROLES::VIEW')")
  public ResponseEntity<GetSingleRoleResponse> getSingle(
      @PathVariable(value = "roleName") String roleName) {
    GetSingleRoleResponse resp = new GetSingleRoleResponse();
    resp.setRole(roleService.getRole(roleName));
    return ResponseEntity.ok(resp);
  }

}
