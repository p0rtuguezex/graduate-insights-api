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
public class QuestionOptionRequest {
  private Long id;

  @NotBlank(message = "El texto de la opción es requerido")
  @Size(max = 255, message = "El texto de la opción no puede exceder los 255 caracteres")
  private String optionText;

  @NotNull(message = "El orden de la opción es requerido")
  private Integer orderNumber;
}
