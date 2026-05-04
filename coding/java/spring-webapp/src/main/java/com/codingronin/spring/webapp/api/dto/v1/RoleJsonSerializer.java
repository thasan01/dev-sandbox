package com.codingronin.spring.webapp.api.dto.v1;

import static com.codingronin.spring.webapp.api.util.CollectionsUtil.nullSafe;
import java.io.IOException;
import java.lang.reflect.Field;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;
import com.codingronin.spring.webapp.api.model.v1.Permission;
import com.codingronin.spring.webapp.api.model.v1.Role;
import com.codingronin.spring.webapp.api.model.v1.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class RoleJsonSerializer extends JsonSerializer<Role> {

  static Logger log = LoggerFactory.getLogger(RoleJsonSerializer.class);

  @Override
  public void serialize(Role role, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeStartObject();

    Field[] fieldArray = role.getClass().getDeclaredFields();
    for (int i = 0; i < fieldArray.length; i++) {
      Field field = fieldArray[i];
      String fieldName = field.getName();

      try {
        if ("members".equals(fieldName))
          processMembers(role, gen, fieldName);
        else if ("permissions".equals(fieldName))
          processPermissions(role, gen, fieldName);
        else
          gen.writeObjectField(fieldName, PropertyUtils.getSimpleProperty(role, fieldName));

      } catch (Exception ex) {
        log.error("Encountered an error while processing field {}.", fieldName, ex);
      }
    }

    gen.writeEndObject();
  }

  void processMembers(Role role, JsonGenerator gen, String fieldName) throws IOException {
    gen.writeArrayFieldStart(fieldName);
    for (User user : nullSafe(role.getMembers())) {
      gen.writeString(user.getUserName());
    }
    gen.writeEndArray();
  }

  void processPermissions(Role role, JsonGenerator gen, String fieldName) throws IOException {
    gen.writeArrayFieldStart(fieldName);
    for (Permission permission : nullSafe(role.getPermissions())) {
      gen.writeString(permission.getName());
    }
    gen.writeEndArray();
  }

}
