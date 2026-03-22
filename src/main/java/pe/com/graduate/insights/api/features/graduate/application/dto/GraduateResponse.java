package pe.com.graduate.insights.api.features.graduate.application.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;

@SuperBuilder
@Getter
@ToString
public class GraduateResponse extends UserResponse {
  private Long graduateId;
  private String fechaInicio;
  private String fechaFin;
  private String cv;
  private String cvPath;
  private Boolean validated;
}
