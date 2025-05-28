package pe.com.graduate.insights.api.domain.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class EducationCenterResponse {
  private Long educationCenterId;
  private String estado;
  private String nombre;
  private String direccion;
}
