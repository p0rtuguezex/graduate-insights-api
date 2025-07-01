package pe.com.graduate.insights.api.domain.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class JobOffersResponse {
  private Long jobOffersId;
  private String titulo;
  private String link;
  private String descripcion;
  private String estado;

  // Información del empleador (visible solo para directores)
  private Long employerId;
  private String employerName;
}
