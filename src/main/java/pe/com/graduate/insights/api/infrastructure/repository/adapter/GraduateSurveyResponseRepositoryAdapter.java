package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.GraduateSurveyResponseRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateSurveyResponseEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionOptionEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateSurveyResponseRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.QuestionOptionRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.GraduateSurveyResponseMapper;

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
  public List<GraduateSurveyResponseEntity> findBySurveyId(Long surveyId) {
    return graduateSurveyResponseRepository.findBySurveyId(surveyId);
  }

  @Override
  public List<GraduateSurveyResponseEntity> findByGraduateId(Long graduateId) {
    return graduateSurveyResponseRepository.findByGraduateId(graduateId);
  }
}
