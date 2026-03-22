package pe.com.graduate.insights.api.features.employer.application.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;

@SuperBuilder
@Getter
@ToString
public class EmployerResponse extends UserResponse {
  private Long employerId;
  private String ruc;
  private String razonSocial;
}
