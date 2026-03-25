package pe.com.graduate.insights.api.features.graduate.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GraduateWorkTrajectoryResponse {
  private String empresa;
  private String cargo;
  private String modalidad;
  private String fechaInicio;
  private String fechaFin;
}
