package com.codingronin.spring.webapp.scim.model.v2;

import java.util.List;
import com.bettercloud.scim2.common.annotations.Attribute;
import com.bettercloud.scim2.common.annotations.Schema;
import com.bettercloud.scim2.common.types.AttributeDefinition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(id = "urn:ietf:params:codingronin:schemas:WebApp:1.0:User", name = "WebAppUser",
    description = "User's WebApp Account")
public class WebAppUser {

  @Attribute(description = "List of direct permissions user has on the WebApp.", isRequired = false,
      mutability = AttributeDefinition.Mutability.READ_WRITE,
      returned = AttributeDefinition.Returned.DEFAULT,
      uniqueness = AttributeDefinition.Uniqueness.NONE)
  List<String> directPermissions;
}
