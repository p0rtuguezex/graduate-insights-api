package pe.com.graduate.insights.api.features.graduate.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GraduateLanguageResponse {
  private Long idiomaId;
  private String nivel;
  private String fechaInicio;
  private String fechaFin;
  private String aprendizaje;
}
