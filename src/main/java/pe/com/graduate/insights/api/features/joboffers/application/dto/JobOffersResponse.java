package pe.com.graduate.insights.api.features.joboffers.application.dto;

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
  private Long employerId;
  private String employerName;
  private String employerRuc;
  private String employerDireccion;
  private String employerCorreo;
}
