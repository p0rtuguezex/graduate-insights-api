package pe.com.graduate.insights.api.features.graduate.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraduateTitulationRequest {

  private Long modalidadTitulacionId;

  private String modalidadOtro;
}
