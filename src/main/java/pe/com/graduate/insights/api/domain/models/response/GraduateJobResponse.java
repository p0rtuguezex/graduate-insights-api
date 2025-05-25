package pe.com.graduate.insights.api.domain.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class GraduateJobResponse {
  private Long jobId;
  private String compania;
  private String estado;
  private String cargo;
  private String modalidad;
  private String fechaInicio;
  private String fechaFin;
}
