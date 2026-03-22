package pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.adapter;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.output.GraduateSurveyResponseRepositoryPort;
import pe.com.graduate.insights.api.features.graduatesurveys.domain.model.GraduateQuestionResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.domain.model.GraduateSurveyResponse;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.entity.GraduateSurveyResponseEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.QuestionOptionEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.jpa.GraduateSurveyResponseRepository;
import pe.com.graduate.insights.api.features.survey.infrastructure.jpa.QuestionOptionRepository;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.mapper.GraduateSurveyResponseMapper;

@Component
@RequiredArgsConstructor
public class GraduateSurveyResponseRepositoryAdapter
    implements GraduateSurveyResponseRepositoryPort {

  private final GraduateSurveyResponseRepository graduateSurveyResponseRepository;
  private final GraduateRepository graduateRepository;
  private final QuestionOptionRepository questionOptionRepository;
  private final GraduateSurveyResponseMapper graduateSurveyResponseMapper;

  @Override
  public void save(GraduateSurveyResponseRequest request, Long graduateId) {
    GraduateSurveyResponseEntity responseEntity = graduateSurveyResponseMapper.toEntity(request);

    // Obtener y establecer la entidad Graduate
    GraduateEntity graduate =
        graduateRepository
            .findById(graduateId)
            .orElseThrow(
                () -> new NotFoundException("Graduado no encontrado con ID: " + graduateId));
    responseEntity.setGraduate(graduate);

    // Procesar las respuestas individuales
    if (request.getResponses() != null) {
      responseEntity.setResponses(
          request.getResponses().stream()
              .map(
                  questionResponse -> {
                    var questionResponseEntity =
                        graduateSurveyResponseMapper.toEntity(questionResponse);
                    questionResponseEntity.setGraduateSurveyResponse(responseEntity);

                    // Procesar las opciones seleccionadas si existen
                    if (questionResponse.getSelectedOptionIds() != null
                        && !questionResponse.getSelectedOptionIds().isEmpty()) {
                      List<QuestionOptionEntity> selectedOptions =
                          questionResponse.getSelectedOptionIds().stream()
                              .map(
                                  optionId ->
                                      questionOptionRepository
                                          .findById(optionId)
                                          .orElseThrow(
                                              () ->
                                                  new NotFoundException(
                                                      "Opción no encontrada con ID: " + optionId)))
                              .collect(Collectors.toList());
                      questionResponseEntity.setSelectedOptions(selectedOptions);
                    }

                    return questionResponseEntity;
                  })
              .collect(Collectors.toList()));
    }

    graduateSurveyResponseRepository.save(responseEntity);
  }

  @Override
  public List<GraduateSurveyResponse> findBySurveyId(Long surveyId) {
    return graduateSurveyResponseRepository.findBySurveyId(surveyId).stream()
        .map(this::toDomain)
        .toList();
  }

  @Override
  public List<GraduateSurveyResponse> findByGraduateId(Long graduateId) {
    return graduateSurveyResponseRepository.findByGraduateId(graduateId).stream()
        .map(this::toDomain)
        .toList();
  }

  private GraduateSurveyResponse toDomain(GraduateSurveyResponseEntity entity) {
    List<GraduateQuestionResponse> responses =
        entity.getResponses() == null
            ? List.of()
            : entity.getResponses().stream().map(this::toQuestionDomain).toList();

    return GraduateSurveyResponse.builder()
        .id(entity.getId())
        .surveyId(entity.getSurvey() != null ? entity.getSurvey().getId() : null)
        .graduateId(entity.getGraduate() != null ? entity.getGraduate().getId() : null)
        .responses(responses)
        .submittedAt(entity.getSubmittedAt())
        .completed(entity.isCompleted())
        .build();
  }

  private GraduateQuestionResponse toQuestionDomain(
      pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.entity.GraduateQuestionResponseEntity
          response) {
    Long questionId = response.getQuestion() != null ? response.getQuestion().getId() : null;
    List<Long> selectedOptionIds =
        response.getSelectedOptions() == null
            ? List.of()
            : response.getSelectedOptions().stream().map(option -> option.getId()).toList();

    return GraduateQuestionResponse.builder()
        .id(response.getId())
        .questionId(questionId)
        .selectedOptionIds(selectedOptionIds)
        .textResponse(response.getTextResponse())
        .numericResponse(response.getNumericResponse())
        .build();
  }
}





