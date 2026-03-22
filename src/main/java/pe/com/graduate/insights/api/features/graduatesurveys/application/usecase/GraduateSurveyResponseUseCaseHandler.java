package pe.com.graduate.insights.api.features.graduatesurveys.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.input.GraduateSurveyResponseUseCase;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.output.GraduateSurveyResponseRepositoryPort;
import pe.com.graduate.insights.api.features.graduatesurveys.domain.model.GraduateSurveyResponse;

@Service
@RequiredArgsConstructor
public class GraduateSurveyResponseUseCaseHandler implements GraduateSurveyResponseUseCase {

  private final GraduateSurveyResponseRepositoryPort graduateSurveyResponseRepositoryPort;

  @Override
  public void save(GraduateSurveyResponseRequest request, Long graduateId) {
    graduateSurveyResponseRepositoryPort.save(request, graduateId);
  }

  @Override
  public List<GraduateSurveyResponse> findBySurveyId(Long surveyId) {
    return graduateSurveyResponseRepositoryPort.findBySurveyId(surveyId);
  }

  @Override
  public List<GraduateSurveyResponse> findByGraduateId(Long graduateId) {
    return graduateSurveyResponseRepositoryPort.findByGraduateId(graduateId);
  }
}