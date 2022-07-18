package com.codingronin.spring.webapp.api.service;

import java.util.List;
import com.codingronin.spring.webapp.api.model.v1.User;

public interface UserService {

  public List<User> getUsers(int page, int size);

  public List<User> createUsers(List<User> users);

  public void deleteUsers(List<String> userNames);

}
