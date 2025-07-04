package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.QuestionOptionRequest;
import pe.com.graduate.insights.api.domain.models.request.QuestionRequest;
import pe.com.graduate.insights.api.domain.models.request.SurveyRequest;
import pe.com.graduate.insights.api.domain.models.response.QuestionOptionResponse;
import pe.com.graduate.insights.api.domain.models.response.QuestionResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionOptionEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyEntity;

@Mapper(
    componentModel = "spring",
    uses = {SurveyTypeMapper.class})
public interface SurveyMapper {

  SurveyMapper INSTANCE = Mappers.getMapper(SurveyMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  SurveyResponse toDomain(SurveyEntity surveyEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  QuestionResponse toDomain(QuestionEntity questionEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  QuestionOptionResponse toDomain(QuestionOptionEntity optionEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "questions", source = "questions")
  @Mapping(target = "surveyType", ignore = true)
  SurveyEntity toEntity(SurveyRequest surveyRequest);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "id", ignore = true)
  QuestionEntity toEntity(QuestionRequest questionRequest);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "id", ignore = true)
  QuestionOptionEntity toEntity(QuestionOptionRequest optionRequest);

  @Mapping(target = "questions", ignore = true)
  @Mapping(target = "surveyType", ignore = true)
  void updateSurveyEntity(SurveyRequest request, @MappingTarget SurveyEntity entity);

  @Mapping(target = "options", ignore = true)
  void updateQuestionEntity(QuestionRequest request, @MappingTarget QuestionEntity entity);

  void updateQuestionOptionEntity(
      QuestionOptionRequest request, @MappingTarget QuestionOptionEntity entity);

  List<SurveyResponse> toDomainList(List<SurveyEntity> entities);

  @AfterMapping
  default void linkSurveyRelations(@MappingTarget SurveyEntity survey) {
    if (survey.getQuestions() != null) {
      survey
          .getQuestions()
          .forEach(
              question -> {
                question.setSurvey(survey);
                if (question.getOptions() != null) {
                  question.getOptions().forEach(option -> option.setQuestion(question));
                }
              });
    }
  }

  @AfterMapping
  default void updateQuestions(SurveyRequest request, @MappingTarget SurveyEntity entity) {
    if (request.getQuestions() == null) {
      return;
    }

    // Si no hay preguntas existentes, crear nuevas
    if (entity.getQuestions() == null) {
      entity.setQuestions(new ArrayList<>());
    }

    // Crear un mapa de las preguntas existentes por ID (si tienen ID)
    Map<Long, QuestionEntity> existingQuestionsMap =
        entity.getQuestions().stream()
            .filter(q -> q.getId() != null)
            .collect(Collectors.toMap(QuestionEntity::getId, q -> q));

    // Crear un set con los IDs de preguntas en el request
    Set<Long> requestQuestionIds =
        request.getQuestions().stream()
            .map(QuestionRequest::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    // Eliminar preguntas que no están en el request (pero solo si no tienen respuestas)
    Iterator<QuestionEntity> iterator = entity.getQuestions().iterator();
    while (iterator.hasNext()) {
      QuestionEntity question = iterator.next();
      if (question.getId() != null && !requestQuestionIds.contains(question.getId())) {
        // Solo eliminar si la pregunta no tiene respuestas asociadas
        // Para mayor seguridad, por ahora no eliminamos preguntas existentes
        // iterator.remove();
      }
    }

    // Procesar preguntas del request
    for (QuestionRequest questionRequest : request.getQuestions()) {
      QuestionEntity questionEntity;

      if (questionRequest.getId() != null
          && existingQuestionsMap.containsKey(questionRequest.getId())) {
        // Actualizar pregunta existente
        questionEntity = existingQuestionsMap.get(questionRequest.getId());
        updateQuestionEntity(questionRequest, questionEntity);
        updateQuestionOptions(questionRequest, questionEntity);
      } else {
        // Crear nueva pregunta
        questionEntity = toEntity(questionRequest);
        questionEntity.setSurvey(entity);
        entity.getQuestions().add(questionEntity);
      }
    }

    // Establecer las relaciones
    linkSurveyRelations(entity);
  }

  default void updateQuestionOptions(QuestionRequest request, QuestionEntity entity) {
    if (request.getOptions() == null) {
      return;
    }

    // Si no hay opciones existentes, crear nuevas
    if (entity.getOptions() == null) {
      entity.setOptions(new ArrayList<>());
    }

    // Crear un mapa de las opciones existentes por ID
    Map<Long, QuestionOptionEntity> existingOptionsMap =
        entity.getOptions().stream()
            .filter(o -> o.getId() != null)
            .collect(Collectors.toMap(QuestionOptionEntity::getId, o -> o));

    // Crear un set con los IDs de opciones en el request
    Set<Long> requestOptionIds =
        request.getOptions().stream()
            .map(QuestionOptionRequest::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    // Eliminar opciones que no están en el request
    Iterator<QuestionOptionEntity> iterator = entity.getOptions().iterator();
    while (iterator.hasNext()) {
      QuestionOptionEntity option = iterator.next();
      if (option.getId() != null && !requestOptionIds.contains(option.getId())) {
        iterator.remove();
      }
    }

    // Procesar opciones del request
    for (QuestionOptionRequest optionRequest : request.getOptions()) {
      QuestionOptionEntity optionEntity;

      if (optionRequest.getId() != null && existingOptionsMap.containsKey(optionRequest.getId())) {
        // Actualizar opción existente
        optionEntity = existingOptionsMap.get(optionRequest.getId());
        updateQuestionOptionEntity(optionRequest, optionEntity);
      } else {
        // Crear nueva opción
        optionEntity = toEntity(optionRequest);
        optionEntity.setQuestion(entity);
        entity.getOptions().add(optionEntity);
      }
    }
  }
}
