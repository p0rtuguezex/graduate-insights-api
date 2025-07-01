package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class JobOffersRequest implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "El título no puede estar vacio")
  private String titulo;

  @NotNull(message = "El link no puede estar vacio")
  private String link;

  @NotNull(message = "La descripcion no puede estar vacio")
  private String descripcion;

  private Long employerId;
}
