package com.codingronin.spring.webapp.api.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.codingronin.spring.webapp.api.model.v1.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

  Page<User> findAll(Pageable pageable);

  User findByUserName(String userName);

  Optional<User> findById(Integer id);

  void deleteByUserName(String userName);
}
