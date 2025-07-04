package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
}
