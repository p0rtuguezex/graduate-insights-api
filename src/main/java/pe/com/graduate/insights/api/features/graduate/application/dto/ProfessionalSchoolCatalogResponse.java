package pe.com.graduate.insights.api.features.graduate.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfessionalSchoolCatalogResponse {
  private Long id;
  private Long facultadId;
  private String nombre;
}
