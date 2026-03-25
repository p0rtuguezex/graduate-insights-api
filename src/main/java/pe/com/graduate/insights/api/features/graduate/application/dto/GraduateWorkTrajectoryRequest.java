package pe.com.graduate.insights.api.features.graduate.application.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraduateWorkTrajectoryRequest {

  private String empresa;

  private String cargo;

  private String modalidad;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaInicio;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaFin;
}
