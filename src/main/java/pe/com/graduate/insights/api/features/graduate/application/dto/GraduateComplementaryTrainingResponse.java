package pe.com.graduate.insights.api.features.graduate.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GraduateComplementaryTrainingResponse {
  private String nombreCurso;
  private String institucion;
  private String fechaInicio;
  private String fechaFin;
}
