package pe.com.graduate.insights.api.domain.models.response;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class GraduateResponse {
  private Long id;
  private Long userId;
  private Date fechaInicio;
  private String fechaFin;
  private String cv;
}
