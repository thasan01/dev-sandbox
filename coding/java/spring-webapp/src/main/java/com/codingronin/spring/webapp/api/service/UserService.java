package com.codingronin.spring.webapp.api.service;

import java.util.List;
import java.util.Map;
import com.codingronin.spring.webapp.api.model.http.v1.CreateUser;
import com.codingronin.spring.webapp.api.model.http.v1.UserAttributes;
import com.codingronin.spring.webapp.api.model.v1.EntitlementMembership;
import com.codingronin.spring.webapp.api.model.v1.User;

public interface UserService {

  public User getUser(String userName);

  public User getUser(Integer id);

  public List<User> getUsers(int page, int size);

  public List<User> createUsers(List<CreateUser> users);

  public void deleteUsers(List<String> userNames);

  public User updateAttributes(String jobId, String userName, UserAttributes userAttributes);

  public List<User> updateAttributes(String jobId, Map<String, UserAttributes> userAttributeMap);

  public User updateEntitlementMemberships(String userName,
      List<EntitlementMembership> entitlementMemberships);

  public User updateEntitlementMemberships(User user,
      List<EntitlementMembership> entitlementMemberships);
}
