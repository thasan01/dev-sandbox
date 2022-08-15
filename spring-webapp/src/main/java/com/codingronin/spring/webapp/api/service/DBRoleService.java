package com.codingronin.spring.webapp.api.service;

import static com.codingronin.spring.webapp.api.util.ObjectsUtil.toList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.codingronin.spring.webapp.api.model.v1.Role;
import com.codingronin.spring.webapp.api.repository.RoleRepository;

@Service
public class DBRoleService implements RoleService {

  @Autowired
  RoleRepository roleRepo;

  @Override
  public Role getRole(String roleName) {
    return roleRepo.findByName(roleName);
  }

  @Override
  public List<Role> getRoles(int page, int size) {
    return toList(roleRepo.findAll(PageRequest.of(page, size)));
  }

}
