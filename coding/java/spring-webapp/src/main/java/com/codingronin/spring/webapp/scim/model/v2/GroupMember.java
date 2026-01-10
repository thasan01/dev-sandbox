package com.codingronin.spring.webapp.scim.model.v2;

import java.io.Serializable;
import java.net.URI;
import com.bettercloud.scim2.common.annotations.Attribute;
import com.bettercloud.scim2.common.types.AttributeDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupMember implements Serializable {

  private static final long serialVersionUID = 1L;

  @Attribute(description = "The URI of the corresponding Group resource", isRequired = true,
      isCaseExact = false, mutability = AttributeDefinition.Mutability.READ_ONLY,
      returned = AttributeDefinition.Returned.DEFAULT,
      uniqueness = AttributeDefinition.Uniqueness.NONE)
  @JsonProperty("$ref")
  private URI ref;

  @Attribute(description = "The id of the SCIM resource", isRequired = true, isCaseExact = true,
      mutability = AttributeDefinition.Mutability.READ_ONLY,
      returned = AttributeDefinition.Returned.DEFAULT,
      uniqueness = AttributeDefinition.Uniqueness.NONE)
  String id;
}
