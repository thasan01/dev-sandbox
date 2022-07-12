package com.codingronin.spring.webapp.api.service;

import java.util.List;
import com.codingronin.spring.webapp.api.model.v1.User;

public interface UserService {

  List<User> getUsers(int startIndex, int count);

}
