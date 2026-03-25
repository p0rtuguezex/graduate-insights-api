package pe.com.graduate.insights.api.features.surveystatistics.infrastructure.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.survey.domain.model.QuestionType;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;
import pe.com.graduate.insights.api.features.surveystatistics.domain.port.output.SurveyStatisticsRepositoryPort;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.entity.GraduateQuestionResponseEntity;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.entity.GraduateSurveyResponseEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyEntity;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.jpa.GraduateQuestionResponseRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.jpa.GraduateSurveyResponseRepository;
import pe.com.graduate.insights.api.features.survey.infrastructure.jpa.SurveyRepository;

@Component
@RequiredArgsConstructor
public class SurveyStatisticsRepositoryAdapter implements SurveyStatisticsRepositoryPort {

  private final SurveyRepository surveyRepository;
  private final GraduateRepository graduateRepository;
  private final GraduateSurveyResponseRepository graduateSurveyResponseRepository;
  private final GraduateQuestionResponseRepository graduateQuestionResponseRepository;

  @Override
  public long countSurveys() {
    return surveyRepository.count();
  }

  @Override
  public long countSurveysByStatus(SurveyStatus status) {
    return surveyRepository.findByStatus(toEntityStatus(status)).size();
  }

  @Override
  public List<Long> findRecentSurveyIds(int limit) {
    return surveyRepository.findAll().stream()
        .sorted((s1, s2) -> s2.getCreatedDate().compareTo(s1.getCreatedDate()))
        .limit(limit)
        .map(SurveyEntity::getId)
        .toList();
  }

  @Override
  public long countActiveGraduates() {
    return graduateRepository.countByUserEstado("1");
  }

  @Override
  public List<SurveyResponseData> findSurveyResponsesBySurveyId(Long surveyId) {
    return graduateSurveyResponseRepository.findBySurveyId(surveyId).stream()
        .map(this::toSurveyResponseData)
        .toList();
  }

  @Override
  public List<SurveyResponseData> findCompletedSurveyResponsesBySurveyId(Long surveyId) {
    return graduateSurveyResponseRepository.findBySurveyIdAndCompleted(surveyId, true).stream()
        .map(this::toSurveyResponseData)
        .toList();
  }

  @Override
  public List<SurveyResponseData> findAllSurveyResponses() {
    return graduateSurveyResponseRepository.findAll().stream().map(this::toSurveyResponseData).toList();
  }

  @Override
  public List<QuestionResponseData> findQuestionResponsesBySurveyId(Long surveyId) {
    return graduateQuestionResponseRepository.findBySurveyId(surveyId).stream()
        .map(this::toQuestionResponseData)
        .toList();
  }

  @Override
  public List<QuestionResponseData> findQuestionResponsesBySurveyIdAndQuestionId(
      Long surveyId, Long questionId) {
    return graduateQuestionResponseRepository.findBySurveyIdAndQuestionId(surveyId, questionId).stream()
        .map(this::toQuestionResponseData)
        .toList();
  }

  @Override
  public Long countQuestionResponsesByQuestionId(Long questionId) {
    return graduateQuestionResponseRepository.countResponsesByQuestionId(questionId);
  }

  @Override
  public List<Integer> findNumericResponsesByQuestionId(Long questionId) {
    return graduateQuestionResponseRepository.findNumericResponsesByQuestionId(questionId);
  }

  @Override
  public List<String> findTextResponsesByQuestionId(Long questionId) {
    return graduateQuestionResponseRepository.findTextResponsesByQuestionId(questionId);
  }

  @Override
  public Map<String, Long> countResponsesByOptionForQuestion(Long questionId) {
    List<Object[]> results = graduateQuestionResponseRepository.countResponsesByOptionForQuestion(questionId);
    Map<String, Long> counts = new HashMap<>();
    for (Object[] result : results) {
      counts.put((String) result[0], (Long) result[1]);
    }
    return counts;
  }

  private SurveyResponseData toSurveyResponseData(GraduateSurveyResponseEntity response) {
    Integer graduationYear = null;
    if (response.getGraduate().getAnioEgreso() != null) {
      try { graduationYear = Integer.parseInt(response.getGraduate().getAnioEgreso()); }
      catch (NumberFormatException ignored) { /* anioEgreso no es numerico */ }
    }
    String gender = response.getGraduate().getUser().getGenero();
    String fullName =
        response.getGraduate().getUser().getNombres()
            + " "
            + response.getGraduate().getUser().getApellidos();

    String departamento = response.getGraduate().getDepartamento();

    // Determine employment status: graduate has a current job if at least one
    // work trajectory has fechaFin IS NULL (currently employed)
    Boolean hasCurrentJob = null;
    if (response.getGraduate().getTrayectoriasLaborales() != null
        && !response.getGraduate().getTrayectoriasLaborales().isEmpty()) {
      hasCurrentJob = response.getGraduate().getTrayectoriasLaborales().stream()
          .anyMatch(t -> t.getFechaFin() == null);
    }

    return new SurveyResponseData(response.getSubmittedAt(), graduationYear, gender, fullName, departamento, hasCurrentJob);
  }

  private QuestionResponseData toQuestionResponseData(GraduateQuestionResponseEntity response) {
    Integer graduationYear = null;
    if (response.getGraduateSurveyResponse().getGraduate().getAnioEgreso() != null) {
      try { graduationYear = Integer.parseInt(response.getGraduateSurveyResponse().getGraduate().getAnioEgreso()); }
      catch (NumberFormatException ignored) { /* anioEgreso no es numerico */ }
    }
    String gender = response.getGraduateSurveyResponse().getGraduate().getUser().getGenero();
    String fullName =
        response.getGraduateSurveyResponse().getGraduate().getUser().getNombres()
            + " "
            + response.getGraduateSurveyResponse().getGraduate().getUser().getApellidos();

    return new QuestionResponseData(
        response.getQuestion().getId(),
        response.getQuestion().getQuestionText(),
        QuestionType.valueOf(response.getQuestion().getQuestionType().name()),
        response.getQuestion().isRequired(),
        response.getSelectedOptions() == null
            ? List.of()
            : response.getSelectedOptions().stream().map(option -> option.getOptionText()).toList(),
        response.getTextResponse(),
        response.getNumericResponse(),
        response.getGraduateSurveyResponse().getSubmittedAt(),
        graduationYear,
        gender,
        fullName);
  }

  private pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus toEntityStatus(
      SurveyStatus status) {
    return pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus.valueOf(status.name());
  }
}


