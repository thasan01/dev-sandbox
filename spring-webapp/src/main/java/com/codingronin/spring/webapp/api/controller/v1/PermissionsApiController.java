package com.codingronin.spring.webapp.api.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.codingronin.spring.webapp.api.controller.RestApiController;
import com.codingronin.spring.webapp.api.model.http.v1.GetPermissionResponse;
import com.codingronin.spring.webapp.api.model.http.v1.GetSinglePermissionResponse;
import com.codingronin.spring.webapp.api.service.PermissionService;

@RestController
@RequestMapping("/api/rest/v1/Permissions")
public class PermissionsApiController extends RestApiController {
  @Autowired
  PermissionService permissionService;

  @GetMapping
  @PreAuthorize("hasAnyAuthority('*::*::*', 'API::*::VIEW', 'API::ROLES::VIEW')")
  public ResponseEntity<GetPermissionResponse> get(//
      @RequestParam(required = true) int page, //
      @RequestParam(defaultValue = DEFAULT_GET_SIZE) int size//
  ) {
    GetPermissionResponse resp = new GetPermissionResponse();
    resp.setPermissions(permissionService.getPermissions(page, size));
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/{permissionName}")
  @PreAuthorize("hasAnyAuthority('*::*::*', 'API::*::VIEW', 'API::ROLES::VIEW')")
  public ResponseEntity<GetSinglePermissionResponse> getSingle(
      @PathVariable(value = "permissionName") String permissionName) {
    GetSinglePermissionResponse resp = new GetSinglePermissionResponse();
    resp.setPermission(permissionService.getPermission(permissionName));
    return ResponseEntity.ok(resp);
  }

}
