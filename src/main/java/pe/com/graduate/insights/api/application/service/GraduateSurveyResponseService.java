package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.GraduateSurveyResponseUseCase;
import pe.com.graduate.insights.api.application.ports.output.GraduateSurveyResponseRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateSurveyResponseEntity;

@Service
@RequiredArgsConstructor
public class GraduateSurveyResponseService implements GraduateSurveyResponseUseCase {

  private final GraduateSurveyResponseRepositoryPort graduateSurveyResponseRepositoryPort;

  @Override
  public void save(GraduateSurveyResponseRequest request, Long graduateId) {
    graduateSurveyResponseRepositoryPort.save(request, graduateId);
  }

  @Override
  public List<GraduateSurveyResponseEntity> findBySurveyId(Long surveyId) {
    return graduateSurveyResponseRepositoryPort.findBySurveyId(surveyId);
  }

  @Override
  public List<GraduateSurveyResponseEntity> findByGraduateId(Long graduateId) {
    return graduateSurveyResponseRepositoryPort.findByGraduateId(graduateId);
  }
}
