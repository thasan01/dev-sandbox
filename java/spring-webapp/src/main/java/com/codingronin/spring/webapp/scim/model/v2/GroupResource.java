package com.codingronin.spring.webapp.scim.model.v2;

import java.util.List;
import com.bettercloud.scim2.common.BaseScimResource;
import com.bettercloud.scim2.common.annotations.Attribute;
import com.bettercloud.scim2.common.annotations.Schema;
import com.bettercloud.scim2.common.types.AttributeDefinition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Schema(id = "urn:ietf:params:scim:schemas:core:2.0:Group", name = "Group", description = "Group")
public class GroupResource extends BaseScimResource {

  private static final long serialVersionUID = 1L;

  @Attribute(description = "Displayname of the group.", isRequired = true,
      mutability = AttributeDefinition.Mutability.READ_WRITE,
      returned = AttributeDefinition.Returned.DEFAULT,
      uniqueness = AttributeDefinition.Uniqueness.SERVER)
  String displayName;

  @Attribute(description = "List of users or groups that are members of this group.",
      isRequired = false, mutability = AttributeDefinition.Mutability.READ_WRITE,
      returned = AttributeDefinition.Returned.DEFAULT,
      uniqueness = AttributeDefinition.Uniqueness.NONE)
  private List<GroupMember> members;

}
