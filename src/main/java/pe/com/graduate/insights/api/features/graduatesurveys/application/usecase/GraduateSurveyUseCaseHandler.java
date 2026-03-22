package pe.com.graduate.insights.api.features.graduatesurveys.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyDetailResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyListResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.input.GraduateSurveyUseCase;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.output.GraduateSurveyRepositoryPort;

@Service
@RequiredArgsConstructor
public class GraduateSurveyUseCaseHandler implements GraduateSurveyUseCase {

  private final GraduateSurveyRepositoryPort graduateSurveyRepositoryPort;

  @Override
  public List<GraduateSurveyListResponse> getAllSurveysForGraduate(Long graduateId) {
    return graduateSurveyRepositoryPort.getAllSurveysForGraduate(graduateId);
  }

  @Override
  public List<GraduateSurveyListResponse> getCompletedSurveysForGraduate(Long graduateId) {
    return graduateSurveyRepositoryPort.getCompletedSurveysForGraduate(graduateId);
  }

  @Override
  public List<GraduateSurveyListResponse> getPendingSurveysForGraduate(Long graduateId) {
    return graduateSurveyRepositoryPort.getPendingSurveysForGraduate(graduateId);
  }

  @Override
  public GraduateSurveyDetailResponse getSurveyDetailForGraduate(Long surveyId, Long graduateId) {
    return graduateSurveyRepositoryPort.getSurveyDetailForGraduate(surveyId, graduateId);
  }
}