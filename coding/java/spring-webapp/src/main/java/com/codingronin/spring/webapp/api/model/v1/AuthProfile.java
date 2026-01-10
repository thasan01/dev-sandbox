package com.codingronin.spring.webapp.api.model.v1;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "app_auth_profile")
@DiscriminatorColumn(name = "auth_source_type", discriminatorType = DiscriminatorType.INTEGER)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AuthProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  boolean enabled;
}
