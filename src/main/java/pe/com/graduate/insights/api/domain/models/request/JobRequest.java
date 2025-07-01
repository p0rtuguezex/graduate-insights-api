package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class JobRequest implements Serializable {

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

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaFin;

  private Long graduateId;
}
