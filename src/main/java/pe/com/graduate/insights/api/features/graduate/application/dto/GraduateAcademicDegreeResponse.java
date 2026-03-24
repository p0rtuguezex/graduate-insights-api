package pe.com.graduate.insights.api.features.graduate.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GraduateAcademicDegreeResponse {
  private Long tipoGradoId;
  private Long universidadId;
  private String fechaGrado;
  private String otroGradoNombre;
  private GraduateTitulationResponse titulacion;
}
