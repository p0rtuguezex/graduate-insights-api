package pe.com.graduate.insights.api.domain.models.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraduateSurveyListResponse {
  private Long surveyId;
  private String title;
  private String description;
  private SurveyType surveyType;
  private boolean completed;
  private LocalDateTime submittedAt;
  private LocalDateTime createdDate;
  private int questionCount;
}
