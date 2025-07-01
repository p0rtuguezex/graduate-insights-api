package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyTypeRequest {

  @NotBlank(message = "El nombre del tipo de encuesta es obligatorio")
  @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
  private String name;

  @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
  private String description;

  @NotNull(message = "El estado activo es obligatorio")
  private Boolean active;
}
