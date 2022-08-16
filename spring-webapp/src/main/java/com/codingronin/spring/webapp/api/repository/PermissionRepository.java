package com.codingronin.spring.webapp.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.codingronin.spring.webapp.api.model.v1.Permission;

@Repository
public interface PermissionRepository extends PagingAndSortingRepository<Permission, Integer> {
  Page<Permission> findAll(Pageable pageable);

  Permission findByName(String name);
}
