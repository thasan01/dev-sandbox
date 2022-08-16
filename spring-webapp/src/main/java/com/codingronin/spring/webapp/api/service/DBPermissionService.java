package com.codingronin.spring.webapp.api.service;

import static com.codingronin.spring.webapp.api.util.ObjectsUtil.toList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.codingronin.spring.webapp.api.model.v1.Permission;
import com.codingronin.spring.webapp.api.repository.PermissionRepository;

@Service
public class DBPermissionService implements PermissionService {

  @Autowired
  PermissionRepository premissionRepo;

  @Override
  public Permission getPermission(String permissionName) {
    return premissionRepo.findByName(permissionName);
  }

  @Override
  public List<Permission> getPermissions(int page, int size) {
    return toList(premissionRepo.findAll(PageRequest.of(page, size)));
  }

}
