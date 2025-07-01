package pe.com.graduate.insights.api.domain.models.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponse {
  private Long id;
  private String title;
  private String description;
  private SurveyTypeResponse surveyType;
  private SurveyStatus status;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<QuestionResponse> questions;
}
