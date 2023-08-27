package com.codingronin.spring.webapp.api.model.v1;

import static com.codingronin.spring.webapp.api.model.v1.Constants.INTERNAL_BASIC_AUTH;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue(INTERNAL_BASIC_AUTH)
public class InternalBasicAuthProfile extends AuthProfile {
  String password;
}
