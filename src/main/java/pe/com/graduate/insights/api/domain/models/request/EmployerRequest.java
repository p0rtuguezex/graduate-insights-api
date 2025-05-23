package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployerRequest extends UserRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "el ruc no puede estar vacio")
  private String ruc;

  @NotNull(message = "la razón social no puede estar vacio")
  private String razonSocial;
}
