package pe.com.graduate.insights.api.domain.models.response;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString
public class GraduateResponse extends UserResponse {
  private Long graduateId;
  private String fechaInicio;
  private String fechaFin;
  private String cv;
  private String cvPath;
}
