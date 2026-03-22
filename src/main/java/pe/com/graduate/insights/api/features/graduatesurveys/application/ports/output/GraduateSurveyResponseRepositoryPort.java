package pe.com.graduate.insights.api.features.graduatesurveys.application.ports.output;

import java.util.List;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.features.graduatesurveys.domain.model.GraduateSurveyResponse;

public interface GraduateSurveyResponseRepositoryPort {
  void save(GraduateSurveyResponseRequest request, Long graduateId);

  List<GraduateSurveyResponse> findBySurveyId(Long surveyId);

  List<GraduateSurveyResponse> findByGraduateId(Long graduateId);
}