package com.codingronin.spring.webapp.api.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.codingronin.spring.webapp.api.model.v1.User;

@Service
public class DbUserService implements UserService {

  @Override
  public List<User> getUsers(int startIndex, int count) {

    User user = new User();
    user.setUserName("aaa.bbb");

    List<User> users = new ArrayList<>();
    users.add(user);
    return users;
  }

}
