package com.codingronin.spring.webapp.api.service;

import static com.codingronin.spring.webapp.api.util.ObjectsUtil.nullSafeToString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import com.codingronin.spring.webapp.api.errors.BadInputException;
import com.codingronin.spring.webapp.api.errors.ResourceNotFoundException;
import com.codingronin.spring.webapp.api.model.http.v1.CreateUser;
import com.codingronin.spring.webapp.api.model.http.v1.UserAttributes;
import com.codingronin.spring.webapp.api.model.v1.AuthProfile;
import com.codingronin.spring.webapp.api.model.v1.InternalBasicAuthProfile;
import com.codingronin.spring.webapp.api.model.v1.User;
import com.codingronin.spring.webapp.api.repository.UserRepository;

@Service
public class DbUserService implements UserService {

  static Logger log = LoggerFactory.getLogger(DbUserService.class);

  @Autowired
  UserRepository userRepo;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Override
  public User getUser(String userName) {
    return userRepo.findByUserName(userName);
  }

  @Override
  public List<User> getUsers(int page, int size) {
    return toList(userRepo.findAll(PageRequest.of(page, size)));
  }

  @Override
  public List<User> createUsers(List<CreateUser> users) {

    List<User> dbUsers = users.stream().map(elem -> {
      User user = new User();
      user.setUserName(elem.getUserName());
      user.setEmail(elem.getEmail());

      String inputPass = elem.getPassword();
      if (inputPass != null) {
        InternalBasicAuthProfile auth = new InternalBasicAuthProfile();
        auth.setEnabled(true);
        auth.setPassword(passwordEncoder.encode(inputPass));
        List<AuthProfile> authProfiles = new ArrayList<>();
        authProfiles.add(auth);
        user.setAuthProfiles(authProfiles);
      }

      return user;
    }).collect(Collectors.toList());


    return toList(userRepo.saveAll(dbUsers));
  }

  @Override
  @Transactional // Required to resolve:
                 // "No EntityManager with actual transaction available for current thread"
  public void deleteUsers(List<String> userNames) {
    userNames.forEach(userName -> userRepo.deleteByUserName(userName));
  }

  @Override
  public User updateAttributes(String jobId, String userName, UserAttributes userAttributes) {

    List<FieldError> fieldErrors = new ArrayList<>();
    User user = updateAttributesHelper(jobId, userName, userAttributes, fieldErrors);

    if (!fieldErrors.isEmpty())
      throw new BadInputException(jobId, fieldErrors);

    userRepo.save(user);
    return user;
  }

  @Override
  public List<User> updateAttributes(String jobId, Map<String, UserAttributes> userAttributeMap) {
    List<User> users = new ArrayList<>();
    List<FieldError> fieldErrors = new ArrayList<>();

    for (Entry<String, UserAttributes> entry : userAttributeMap.entrySet()) {
      users.add(updateAttributesHelper(jobId, entry.getKey(), entry.getValue(), fieldErrors));
    }

    if (!fieldErrors.isEmpty())
      throw new BadInputException(jobId, fieldErrors);

    userRepo.saveAll(users);
    return users;
  }


  public User updateAttributesHelper(String jobId, String userName, UserAttributes userAttributes,
      List<FieldError> fieldErrors) {

    User user = userRepo.findByUserName(userName);

    if (user == null)
      throw new ResourceNotFoundException(jobId, userName);

    for (Entry<String, Object> entry : userAttributes.entrySet()) {
      try {
        PropertyUtils.setSimpleProperty(user, entry.getKey(), entry.getValue());
      } catch (ReflectiveOperationException ex) {
        log.error("Encountered error while trying to update attribute {} for user {}. Error={}",
            userName, entry.getKey(), ex.getMessage());

        fieldErrors.add(new FieldError(userName, nullSafeToString(entry.getKey()),
            nullSafeToString(entry.getValue()), false, null, null, ex.getMessage()));
      }
    }

    return user;
  }


  <T> List<T> toList(Iterable<T> iter) {
    return StreamSupport.stream(iter.spliterator(), false).collect(Collectors.toList());
  }

}
