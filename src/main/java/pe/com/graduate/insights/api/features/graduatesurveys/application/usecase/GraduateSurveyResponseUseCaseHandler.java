package pe.com.graduate.insights.api.features.graduatesurveys.application.usecase;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateQuestionResponseRequest;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.input.GraduateSurveyResponseUseCase;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.output.GraduateSurveyResponseRepositoryPort;
import pe.com.graduate.insights.api.features.graduatesurveys.domain.exception.SurveyResponseValidationException;
import pe.com.graduate.insights.api.features.graduatesurveys.domain.model.GraduateSurveyResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.jpa.GraduateSurveyResponseRepository;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.QuestionEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus;
import pe.com.graduate.insights.api.features.survey.infrastructure.jpa.SurveyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class GraduateSurveyResponseUseCaseHandler implements GraduateSurveyResponseUseCase {

  private final GraduateSurveyResponseRepositoryPort graduateSurveyResponseRepositoryPort;
  private final SurveyRepository surveyRepository;
  private final GraduateSurveyResponseRepository graduateSurveyResponseRepository;

  @Override
  public void save(GraduateSurveyResponseRequest request, Long graduateId) {
    // 1. Load survey and verify ACTIVE status
    SurveyEntity survey =
        surveyRepository
            .findById(request.getSurveyId())
            .orElseThrow(() -> new SurveyResponseValidationException("Encuesta no encontrada"));
    if (survey.getStatus() != SurveyStatus.ACTIVE) {
      throw new SurveyResponseValidationException("La encuesta no esta activa");
    }

    // 2. Check duplicate completed submission
    if (graduateSurveyResponseRepository.existsBySurveyIdAndGraduateIdAndCompleted(
        request.getSurveyId(), graduateId, true)) {
      throw new SurveyResponseValidationException("Ya has respondido esta encuesta");
    }

    // 3. Validate required questions have responses
    Set<Long> answeredQuestionIds =
        request.getResponses().stream()
            .map(GraduateQuestionResponseRequest::getQuestionId)
            .collect(Collectors.toSet());

    List<String> missingRequired =
        survey.getQuestions().stream()
            .filter(QuestionEntity::isRequired)
            .filter(q -> !answeredQuestionIds.contains(q.getId()))
            .map(q -> "Pregunta requerida sin respuesta: " + q.getQuestionText())
            .toList();

    if (!missingRequired.isEmpty()) {
      throw new SurveyResponseValidationException(
          "Faltan respuestas obligatorias: " + String.join(", ", missingRequired));
    }

    // 4. Delegate to adapter
    graduateSurveyResponseRepositoryPort.save(request, graduateId);
  }

  @Override
  public void saveDraft(GraduateSurveyResponseRequest request, Long graduateId) {
    // Only validate survey exists and is ACTIVE
    SurveyEntity survey =
        surveyRepository
            .findById(request.getSurveyId())
            .orElseThrow(() -> new SurveyResponseValidationException("Encuesta no encontrada"));
    if (survey.getStatus() != SurveyStatus.ACTIVE) {
      throw new SurveyResponseValidationException("La encuesta no esta activa");
    }

    // Do NOT check required fields (it's a draft)
    // Do NOT check completed duplicates — drafts can be updated
    graduateSurveyResponseRepositoryPort.saveDraft(request, graduateId);
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
