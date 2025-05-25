package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GraduateJobRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "La compania no puede estar vacio")
  private String compania;

  @NotNull(message = "El cargo no puede estar vacio")
  private String cargo;

  @NotNull(message = "La modalidad no puede estar vacio")
  private String modalidad;

  @NotNull(message = "La fecha de inicio no puede ser nula")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaInicio;

  @NotNull(message = "La fecha fin no puede ser nula")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaFin;
}
