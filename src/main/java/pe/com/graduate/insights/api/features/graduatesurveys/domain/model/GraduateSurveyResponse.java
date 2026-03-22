package pe.com.graduate.insights.api.features.graduatesurveys.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GraduateSurveyResponse {
  Long id;
  Long surveyId;
  Long graduateId;
  List<GraduateQuestionResponse> responses;
  LocalDateTime submittedAt;
  boolean completed;
}