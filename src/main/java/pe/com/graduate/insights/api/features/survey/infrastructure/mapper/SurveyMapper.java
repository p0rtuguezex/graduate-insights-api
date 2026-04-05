package pe.com.graduate.insights.api.features.survey.infrastructure.mapper;

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
import pe.com.graduate.insights.api.features.survey.application.dto.QuestionOptionRequest;
import pe.com.graduate.insights.api.features.survey.application.dto.QuestionOptionResponse;
import pe.com.graduate.insights.api.features.survey.application.dto.QuestionRequest;
import pe.com.graduate.insights.api.features.survey.application.dto.QuestionResponse;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyRequest;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;
import pe.com.graduate.insights.api.features.survey.domain.model.QuestionType;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.QuestionEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.QuestionOptionEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyEntity;
import pe.com.graduate.insights.api.features.surveytype.infrastructure.mapper.SurveyTypeMapper;

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
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  void updateSurveyEntity(SurveyRequest request, @MappingTarget SurveyEntity entity);

  @Mapping(target = "options", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "survey", ignore = true)
  void updateQuestionEntity(QuestionRequest request, @MappingTarget QuestionEntity entity);

  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "question", ignore = true)
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

    // Si no hay preguntas existentes, crear nueva lista
    if (entity.getQuestions() == null) {
      entity.setQuestions(new ArrayList<>());
    }

    // Crear un mapa de las preguntas existentes por ID (si tienen ID)
    Map<Long, QuestionEntity> existingQuestionsMap =
        entity.getQuestions().stream()
            .filter(q -> q.getId() != null)
            .collect(Collectors.toMap(QuestionEntity::getId, q -> q));

    // Crear una nueva lista para las preguntas actualizadas
    List<QuestionEntity> updatedQuestions = new ArrayList<>();

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
        // Auto-crear opciones por defecto para preguntas de tipo SCALE sin opciones
        if (QuestionType.SCALE.equals(questionRequest.getQuestionType())
            && (questionRequest.getOptions() == null || questionRequest.getOptions().isEmpty())) {
          questionEntity.setOptions(createDefaultScaleOptions(questionEntity));
        }
      }
      updatedQuestions.add(questionEntity);
    }

    // Reemplazar la lista de preguntas con la lista actualizada
    entity.getQuestions().clear();
    entity.getQuestions().addAll(updatedQuestions);

    // Establecer las relaciones
    linkSurveyRelations(entity);
  }

  default List<QuestionOptionEntity> createDefaultScaleOptions(QuestionEntity question) {
    String[] labels = {"1 - Muy malo", "2 - Malo", "3 - Regular", "4 - Bueno", "5 - Muy bueno"};
    List<QuestionOptionEntity> options = new ArrayList<>();
    for (int i = 0; i < labels.length; i++) {
      QuestionOptionEntity option = new QuestionOptionEntity();
      option.setOptionText(labels[i]);
      option.setOrderNumber(i + 1);
      option.setQuestion(question);
      options.add(option);
    }
    return options;
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
