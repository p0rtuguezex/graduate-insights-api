package pe.com.graduate.insights.api.domain.models.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GraduateSurveyDetailResponse {
  private Long surveyId;
  private String surveyTitle;
  private String surveyDescription;
  private SurveyTypeResponse surveyType;
  private boolean completed;
  private LocalDateTime submittedAt;
  private LocalDateTime createdDate;

  // Preguntas con sus respuestas (si las hay)
  private List<QuestionDetailResponse> questions;
}
