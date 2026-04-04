package pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class GraduateSurveyResponseRepositoryAdapter
    implements GraduateSurveyResponseRepositoryPort {

  private final GraduateSurveyResponseRepository graduateSurveyResponseRepository;
  private final GraduateRepository graduateRepository;
  private final QuestionOptionRepository questionOptionRepository;
  private final GraduateSurveyResponseMapper graduateSurveyResponseMapper;

  @Override
  @Transactional
  public void save(GraduateSurveyResponseRequest request, Long graduateId) {
    // Delete any existing draft for this survey+graduate before saving the completed response
    graduateSurveyResponseRepository
        .findBySurveyIdAndGraduateIdAndCompleted(request.getSurveyId(), graduateId, false)
        .ifPresent(draft -> {
          graduateSurveyResponseRepository.delete(draft);
          graduateSurveyResponseRepository.flush();
        });

    GraduateSurveyResponseEntity responseEntity = graduateSurveyResponseMapper.toEntity(request);

    // Obtener y establecer la entidad Graduate
    GraduateEntity graduate =
        graduateRepository
            .findById(graduateId)
            .orElseThrow(
                () -> new NotFoundException("Graduado no encontrado con ID: " + graduateId));
    responseEntity.setGraduate(graduate);

    // Batch-load all selected options to avoid N+1
    Map<Long, QuestionOptionEntity> optionsMap = loadOptionsMap(request);

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
                                  optionId -> {
                                    QuestionOptionEntity option = optionsMap.get(optionId);
                                    if (option == null) {
                                      throw new NotFoundException(
                                          "Opcion no encontrada con ID: " + optionId);
                                    }
                                    return option;
                                  })
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
  @Transactional
  public void saveDraft(GraduateSurveyResponseRequest request, Long graduateId) {
    // Obtener la entidad Graduate
    GraduateEntity graduate =
        graduateRepository
            .findById(graduateId)
            .orElseThrow(
                () -> new NotFoundException("Graduado no encontrado con ID: " + graduateId));

    // Check if an incomplete draft already exists for this survey+graduate
    Optional<GraduateSurveyResponseEntity> existingDraft =
        graduateSurveyResponseRepository
            .findBySurveyIdAndGraduateIdAndCompleted(request.getSurveyId(), graduateId, false);

    // If a draft exists, delete it (we'll create a fresh one)
    existingDraft.ifPresent(draft -> {
      graduateSurveyResponseRepository.delete(draft);
      graduateSurveyResponseRepository.flush();
    });

    // Create new draft response entity
    GraduateSurveyResponseEntity responseEntity = graduateSurveyResponseMapper.toEntity(request);
    responseEntity.setGraduate(graduate);
    responseEntity.setCompleted(false);
    responseEntity.setSubmittedAt(null);

    // Batch-load all selected options to avoid N+1
    Map<Long, QuestionOptionEntity> optionsMap = loadOptionsMap(request);

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
                                  optionId -> {
                                    QuestionOptionEntity option = optionsMap.get(optionId);
                                    if (option == null) {
                                      throw new NotFoundException(
                                          "Opcion no encontrada con ID: " + optionId);
                                    }
                                    return option;
                                  })
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

  /**
   * Batch-loads all question option entities referenced by the request,
   * avoiding N+1 queries when processing individual question responses.
   */
  private Map<Long, QuestionOptionEntity> loadOptionsMap(GraduateSurveyResponseRequest request) {
    if (request.getResponses() == null) {
      return Map.of();
    }

    List<Long> allOptionIds = request.getResponses().stream()
        .filter(r -> r.getSelectedOptionIds() != null)
        .flatMap(r -> r.getSelectedOptionIds().stream())
        .distinct()
        .toList();

    if (allOptionIds.isEmpty()) {
      return Map.of();
    }

    return questionOptionRepository.findAllById(allOptionIds)
        .stream()
        .collect(Collectors.toMap(QuestionOptionEntity::getId, Function.identity()));
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
