package com.codingronin.spring.webapp.api.service;

import java.util.List;
import com.codingronin.spring.webapp.api.model.v1.Permission;

public interface PermissionService {

  public Permission getPermission(String permissionName);

  public List<Permission> getPermissions(int page, int size);

}
