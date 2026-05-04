package com.codingronin.spring.webapp.scim.dto.v2;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import com.bettercloud.scim2.common.types.Email;
import com.bettercloud.scim2.common.types.Group;
import com.bettercloud.scim2.common.types.UserResource;
import com.codingronin.spring.webapp.api.model.v1.Permission;
import com.codingronin.spring.webapp.api.model.v1.Role;
import com.codingronin.spring.webapp.api.model.v1.User;
import com.codingronin.spring.webapp.api.util.Pair;
import com.codingronin.spring.webapp.scim.model.v2.WebAppUser;

public class ScimUserMapper implements ScimObjectMapper<User, UserResource> {

  @Value("${scim2.baseUrl}")
  String scimBaseUrl;

  @Override
  public User fromScim(UserResource scimUser) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<User> fromScim(List<UserResource> scimUsers) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UserResource toScim(User user) {
    return convertToScimUser(new Pair<>(scimBaseUrl, user));
  }

  @Override
  public List<UserResource> toScim(List<User> users) {

    return users.stream().map(u -> new Pair<>(scimBaseUrl, u))
        .map(ScimUserMapper::convertToScimUser)//
        .collect(Collectors.toList());

  }

  static UserResource convertToScimUser(Pair<String, User> pair) {
    String baseUrl = pair.getFirst();
    User user = pair.getSecond();

    UserResource scimUser = new UserResource();
    scimUser.setId(String.valueOf(user.getId()));
    scimUser.setUserName(user.getUserName());
    scimUser.setActive(User.Status.ACTIVE.equals(user.getStatus()));

    if (user.getEmail() != null) {
      Email email = new Email();
      email.setType("work");
      email.setPrimary(true);
      email.setValue(user.getEmail());
      scimUser.setEmails(Arrays.asList(email));
    }


    if (!CollectionUtils.isEmpty(user.getRoles())) {
      List<Group> scimGroups = user.getRoles().stream().map(r -> new Pair<>(baseUrl, r))
          .map(ScimUserMapper::convertScimGroup).collect(Collectors.toList());
      scimUser.setGroups(scimGroups);
    }

    // Add custom extension if directPermission exists
    if (!CollectionUtils.isEmpty(user.getDirectPermissions())) {
      WebAppUser webAppUser = new WebAppUser();

      List<String> directPermissions = user.getDirectPermissions().stream()//
          .map(Permission::getName)//
          .collect(Collectors.toList());

      webAppUser.setDirectPermissions(directPermissions);
      scimUser.setExtension(webAppUser);
    }


    return scimUser;
  }

  static Group convertScimGroup(Pair<String, Role> pair) {
    String baseUrl = pair.getFirst();
    Role role = pair.getSecond();

    Group scimGroup = new Group();
    scimGroup.setValue(role.getName());
    scimGroup.setRef(URI.create(baseUrl + "/Groups/" + role.getId()));
    scimGroup.setType("Security");
    return scimGroup;
  }

}
