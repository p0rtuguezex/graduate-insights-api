package pe.com.graduate.insights.api.application.ports.output;

import java.util.List;
import pe.com.graduate.insights.api.domain.models.response.GraduateSurveyDetailResponse;
import pe.com.graduate.insights.api.domain.models.response.GraduateSurveyListResponse;

public interface GraduateSurveyRepositoryPort {
  List<GraduateSurveyListResponse> getAllSurveysForGraduate(Long graduateId);

  List<GraduateSurveyListResponse> getCompletedSurveysForGraduate(Long graduateId);

  List<GraduateSurveyListResponse> getPendingSurveysForGraduate(Long graduateId);

  GraduateSurveyDetailResponse getSurveyDetailForGraduate(Long surveyId, Long graduateId);
}
