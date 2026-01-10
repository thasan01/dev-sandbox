package com.codingronin.spring.webapp.api.model.http.v1;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.codingronin.spring.webapp.api.model.v1.EntitlementMembership;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateUserMembershipsRequest extends ApiBaseRequest {
  @NotEmpty
  @NotNull
  @Valid
  List<EntitlementMembership> memberships;
}
