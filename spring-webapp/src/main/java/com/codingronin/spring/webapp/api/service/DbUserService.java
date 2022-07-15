package com.codingronin.spring.webapp.api.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.codingronin.spring.webapp.api.model.v1.User;
import com.codingronin.spring.webapp.api.repository.UserRepository;

@Service
public class DbUserService implements UserService {

  @Autowired
  UserRepository userRepo;

  @Override
  public List<User> getUsers(int page, int size) {
    return toList(userRepo.findAll(PageRequest.of(page, size)));
  }

  @Override
  public List<User> createUsers(List<User> users) {
    return toList(userRepo.saveAll(users));
  }

  <T> List<T> toList(Iterable<T> iter) {
    return StreamSupport.stream(iter.spliterator(), false).collect(Collectors.toList());
  }
}
