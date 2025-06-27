package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraduateQuestionResponseRequest {
  @NotNull(message = "El ID de la pregunta es requerido")
  private Long questionId;

  private List<Long> selectedOptionIds;
  private String textResponse;
  private Integer numericResponse;
}
