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
public class GraduateSurveyResponseRequest {
  @NotNull(message = "El ID de la encuesta es requerido")
  private Long surveyId;

  @NotNull(message = "Las respuestas son requeridas")
  private List<GraduateQuestionResponseRequest> responses;
}
