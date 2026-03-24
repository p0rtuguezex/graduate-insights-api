package pe.com.graduate.insights.api.features.graduate.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogOptionResponse {

  private Long id;

  private String codigo;

  private String nombre;
}
