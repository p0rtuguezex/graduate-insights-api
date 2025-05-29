package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class EventTypesRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "el nombre no puede estar vacio")
  private String nombre;
}
