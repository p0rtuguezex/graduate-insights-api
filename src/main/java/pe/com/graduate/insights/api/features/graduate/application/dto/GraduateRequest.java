package pe.com.graduate.insights.api.features.graduate.application.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GraduateRequest extends UserRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "La fecha de inicio no puede ser nula")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaInicio;

  @NotNull(message = "La fecha fin no puede ser nula")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaFin;

  @NotNull(message = "el cv no puede estar vacio")
  private String cvPath;

  private Boolean validated;
}
