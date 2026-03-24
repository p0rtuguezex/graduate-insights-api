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
public class GraduateAcademicDegreeRequest {

  private Long tipoGradoId;

  private Long universidadId;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaGrado;

  private String otroGradoNombre;

  private GraduateTitulationRequest titulacion;
}
