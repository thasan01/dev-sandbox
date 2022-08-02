package com.codingronin.spring.webapp.api.dto.v1;

import static com.codingronin.spring.webapp.api.util.CollectionsUtil.nullSafe;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;
import com.codingronin.spring.webapp.api.model.v1.Role;
import com.codingronin.spring.webapp.api.model.v1.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class UserJsonSerializer extends JsonSerializer<User> {

  static Logger log = LoggerFactory.getLogger(UserJsonSerializer.class);

  @Override
  public void serialize(User user, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {

    Set<String> ignore = new HashSet<>();
    ignore.addAll(Arrays.asList("authProfiles"));

    gen.writeStartObject();

    Field[] fieldArray = user.getClass().getDeclaredFields();
    for (int i = 0; i < fieldArray.length; i++) {
      Field field = fieldArray[i];
      String fieldName = field.getName();

      if (ignore.contains(fieldName))
        continue;

      try {
        if ("roles".equals(fieldName))
          processRoles(user, gen, fieldName);
        else
          gen.writeObjectField(fieldName, PropertyUtils.getSimpleProperty(user, fieldName));

      } catch (Exception ex) {
        log.error("Encountered an error while processing field {}.", fieldName, ex);
      }
    }

    gen.writeEndObject();
  }

  void processRoles(User user, JsonGenerator gen, String fieldName) throws IOException {
    log.debug("Processing roles for user {}", user.getUserName());
    gen.writeArrayFieldStart(fieldName);
    for (Role role : nullSafe(user.getRoles())) {
      log.debug("Processing role[{}] for user {}", role.getName(), user.getUserName());
      gen.writeString(role.getName());
    }
    gen.writeEndArray();
  }

}
