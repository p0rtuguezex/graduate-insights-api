package pe.com.graduate.insights.api.features.graduatesurveys.application.ports.input;

import java.util.List;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyDetailResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyListResponse;

public interface GraduateSurveyUseCase {
  List<GraduateSurveyListResponse> getAllSurveysForGraduate(Long graduateId);

  List<GraduateSurveyListResponse> getCompletedSurveysForGraduate(Long graduateId);

  List<GraduateSurveyListResponse> getPendingSurveysForGraduate(Long graduateId);

  GraduateSurveyDetailResponse getSurveyDetailForGraduate(Long surveyId, Long graduateId);
}