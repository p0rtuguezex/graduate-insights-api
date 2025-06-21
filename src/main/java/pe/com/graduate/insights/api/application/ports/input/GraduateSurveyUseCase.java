package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.domain.models.response.GraduateSurveyDetailResponse;
import pe.com.graduate.insights.api.domain.models.response.GraduateSurveyListResponse;

import java.util.List;

public interface GraduateSurveyUseCase {
    List<GraduateSurveyListResponse> getAllSurveysForGraduate(Long graduateId);
    List<GraduateSurveyListResponse> getCompletedSurveysForGraduate(Long graduateId);
    List<GraduateSurveyListResponse> getPendingSurveysForGraduate(Long graduateId);
    GraduateSurveyDetailResponse getSurveyDetailForGraduate(Long surveyId, Long graduateId);
} 