package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobOffersRequest implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "El título no puede estar vacio")
  private String titulo;

  @NotNull(message = "El link no puede estar vacio")
  private String link;

  @NotNull(message = "La descripcion no puede estar vacio")
  private String descripcion;
}
