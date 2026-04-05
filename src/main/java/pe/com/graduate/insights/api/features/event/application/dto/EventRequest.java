package pe.com.graduate.insights.api.features.event.application.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "el nombre no puede estar vacio")
  private String nombre;

  @NotNull(message = "el contenido no puede estar vacio")
  private String contenido;

  @NotNull(message = "el directorId no puede estar vacio")
  private Long directorId;

  @NotNull(message = "el eventTypeId no puede estar vacio")
  private Long eventTypeId;

  @NotNull(message = "La fecha del evento es obligatoria")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaEvento;

  private String enlaceInscripcion;
}
