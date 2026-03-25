package pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.adapter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyDetailResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyListResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.QuestionDetailResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.SurveyTypeResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.output.GraduateSurveyRepositoryPort;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.jpa.GraduateSurveyResponseRepository;
import pe.com.graduate.insights.api.features.survey.infrastructure.jpa.SurveyRepository;
import pe.com.graduate.insights.api.features.survey.infrastructure.mapper.SurveyMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class GraduateSurveyRepositoryAdapter implements GraduateSurveyRepositoryPort {

  private final SurveyRepository surveyRepository;
  private final GraduateSurveyResponseRepository graduateSurveyResponseRepository;
  private final SurveyMapper surveyMapper;

  @Override
  public List<GraduateSurveyListResponse> getAllSurveysForGraduate(Long graduateId) {
    // Obtener todas las encuestas activas con sus preguntas y tipo (evita N+1)
    var allSurveys = surveyRepository.findByStatusWithQuestionsAndType(SurveyStatus.ACTIVE);

    // Obtener las encuestas completadas por el graduado
    var completedSurveys = graduateSurveyResponseRepository.findByGraduateId(graduateId);
    Set<Long> completedSurveyIds =
        completedSurveys.stream()
            .map(response -> response.getSurvey().getId())
            .collect(Collectors.toSet());

    return allSurveys.stream()
        .map(
            survey -> {
              boolean isCompleted = completedSurveyIds.contains(survey.getId());
              var completedResponse =
                  completedSurveys.stream()
                      .filter(response -> response.getSurvey().getId().equals(survey.getId()))
                      .findFirst();

              return GraduateSurveyListResponse.builder()
                  .surveyId(survey.getId())
                  .title(survey.getTitle())
                  .description(survey.getDescription())
                  .surveyType(
                      SurveyTypeResponse.builder()
                          .id(survey.getSurveyType().getId())
                          .name(survey.getSurveyType().getName())
                          .description(survey.getSurveyType().getDescription())
                          .active(survey.getSurveyType().getActive())
                          .build())
                  .completed(isCompleted)
                  .submittedAt(
                      completedResponse.map(response -> response.getSubmittedAt()).orElse(null))
                  .createdDate(survey.getCreatedDate())
                  .questionCount(survey.getQuestions() != null ? survey.getQuestions().size() : 0)
                  .build();
            })
        .toList();
  }

  @Override
  public List<GraduateSurveyListResponse> getCompletedSurveysForGraduate(Long graduateId) {
    return getAllSurveysForGraduate(graduateId).stream()
        .filter(GraduateSurveyListResponse::isCompleted)
        .toList();
  }

  @Override
  public List<GraduateSurveyListResponse> getPendingSurveysForGraduate(Long graduateId) {
    return getAllSurveysForGraduate(graduateId).stream()
        .filter(survey -> !survey.isCompleted())
        .toList();
  }

  @Override
  public GraduateSurveyDetailResponse getSurveyDetailForGraduate(Long surveyId, Long graduateId) {
    // Obtener la encuesta
    var survey =
        surveyRepository
            .findById(surveyId)
            .orElseThrow(() -> new NotFoundException("Encuesta no encontrada con ID: " + surveyId));

    // Obtener la respuesta del graduado (si existe)
    var graduateResponse =
        graduateSurveyResponseRepository.findBySurveyIdAndGraduateId(surveyId, graduateId);

    // Mapear las preguntas con sus respuestas
    List<QuestionDetailResponse> questionDetails =
        survey.getQuestions().stream()
            .map(
                question -> {
                  QuestionDetailResponse.QuestionDetailResponseBuilder builder =
                      QuestionDetailResponse.builder()
                          .questionId(question.getId())
                          .questionText(question.getQuestionText())
                          .questionType(
                              pe.com.graduate.insights.api.features.survey.domain.model
                                  .QuestionType.valueOf(question.getQuestionType().name()))
                          .required(question.isRequired())
                          .options(
                              question.getOptions() != null
                                  ? question.getOptions().stream()
                                      .map(surveyMapper::toDomain)
                                      .toList()
                                  : List.of())
                          .answered(false);

                  // Si hay respuesta del graduado, agregar las respuestas
                  if (graduateResponse.isPresent()) {
                    var questionResponse =
                        graduateResponse.get().getResponses().stream()
                            .filter(resp -> resp.getQuestion().getId().equals(question.getId()))
                            .findFirst();

                    if (questionResponse.isPresent()) {
                      var resp = questionResponse.get();
                      builder
                          .answered(true)
                          .textResponse(resp.getTextResponse())
                          .numericResponse(resp.getNumericResponse())
                          .selectedOptionIds(
                              resp.getSelectedOptions() != null
                                  ? resp.getSelectedOptions().stream()
                                      .map(option -> option.getId())
                                      .toList()
                                  : List.of());
                    }
                  }

                  return builder.build();
                })
            .toList();

    // Construir la respuesta
    GraduateSurveyDetailResponse.GraduateSurveyDetailResponseBuilder responseBuilder =
        GraduateSurveyDetailResponse.builder()
            .surveyId(survey.getId())
            .surveyTitle(survey.getTitle())
            .surveyDescription(survey.getDescription())
            .surveyType(
                SurveyTypeResponse.builder()
                    .id(survey.getSurveyType().getId())
                    .name(survey.getSurveyType().getName())
                    .description(survey.getSurveyType().getDescription())
                    .active(survey.getSurveyType().getActive())
                    .build())
            .completed(graduateResponse.isPresent())
            .createdDate(survey.getCreatedDate())
            .questions(questionDetails);

    // Si hay respuesta del graduado, agregar la información de fecha
    if (graduateResponse.isPresent()) {
      var response = graduateResponse.get();
      responseBuilder.submittedAt(response.getSubmittedAt());
    }

    return responseBuilder.build();
  }
}




