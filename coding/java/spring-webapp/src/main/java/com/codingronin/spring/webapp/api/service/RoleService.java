package com.codingronin.spring.webapp.api.service;

import java.util.List;
import com.codingronin.spring.webapp.api.model.v1.Role;

public interface RoleService {

  public Role getRole(String roleName);

  public List<Role> getRoles(int page, int size);

}
