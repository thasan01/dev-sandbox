package com.codingronin.spring.webapp.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.codingronin.spring.webapp.api.model.v1.Role;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

  Page<Role> findAll(Pageable pageable);

  Role findByName(String name);

  void deleteByName(String name);

}
