package pe.com.graduate.insights.api.application.ports.input;

import java.util.List;
import pe.com.graduate.insights.api.domain.models.request.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateSurveyResponseEntity;

public interface GraduateSurveyResponseUseCase {
  void save(GraduateSurveyResponseRequest request, Long graduateId);

  List<GraduateSurveyResponseEntity> findBySurveyId(Long surveyId);

  List<GraduateSurveyResponseEntity> findByGraduateId(Long graduateId);
}
