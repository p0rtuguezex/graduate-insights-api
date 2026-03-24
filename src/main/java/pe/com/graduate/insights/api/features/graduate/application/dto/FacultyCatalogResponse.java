package pe.com.graduate.insights.api.features.graduate.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacultyCatalogResponse {
  private Long id;
  private String nombre;
}
