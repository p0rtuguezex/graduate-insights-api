package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.domain.models.request.QuestionOptionRequest;
import pe.com.graduate.insights.api.domain.models.request.QuestionRequest;
import pe.com.graduate.insights.api.domain.models.request.SurveyRequest;
import pe.com.graduate.insights.api.domain.models.response.QuestionOptionResponse;
import pe.com.graduate.insights.api.domain.models.response.QuestionResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionOptionEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyEntity;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
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
    SurveyEntity toEntity(SurveyRequest surveyRequest);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true)
    QuestionEntity toEntity(QuestionRequest questionRequest);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true)
    QuestionOptionEntity toEntity(QuestionOptionRequest optionRequest);

    @Mapping(target = "questions", ignore = true)
    void updateSurveyEntity(SurveyRequest request, @MappingTarget SurveyEntity entity);

    @Mapping(target = "options", ignore = true)
    void updateQuestionEntity(QuestionRequest request, @MappingTarget QuestionEntity entity);

    void updateQuestionOptionEntity(QuestionOptionRequest request, @MappingTarget QuestionOptionEntity entity);

    List<SurveyResponse> toDomainList(List<SurveyEntity> entities);

    @AfterMapping
    default void linkSurveyRelations(@MappingTarget SurveyEntity survey) {
        if (survey.getQuestions() != null) {
            survey.getQuestions().forEach(question -> {
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
        Map<Long, QuestionEntity> existingQuestionsMap = entity.getQuestions().stream()
                .filter(q -> q.getId() != null)
                .collect(Collectors.toMap(QuestionEntity::getId, q -> q));

        // Lista para las preguntas actualizadas
        List<QuestionEntity> updatedQuestions = new ArrayList<>();

        for (QuestionRequest questionRequest : request.getQuestions()) {
            QuestionEntity questionEntity;
            
            if (questionRequest.getId() != null && existingQuestionsMap.containsKey(questionRequest.getId())) {
                // Actualizar pregunta existente
                questionEntity = existingQuestionsMap.get(questionRequest.getId());
                updateQuestionEntity(questionRequest, questionEntity);
                updateQuestionOptions(questionRequest, questionEntity);
            } else {
                // Crear nueva pregunta
                questionEntity = toEntity(questionRequest);
                questionEntity.setSurvey(entity);
            }
            
            updatedQuestions.add(questionEntity);
        }

        // Limpiar y establecer las preguntas actualizadas
        entity.getQuestions().clear();
        entity.getQuestions().addAll(updatedQuestions);
        
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
        Map<Long, QuestionOptionEntity> existingOptionsMap = entity.getOptions().stream()
                .filter(o -> o.getId() != null)
                .collect(Collectors.toMap(QuestionOptionEntity::getId, o -> o));

        // Lista para las opciones actualizadas
        List<QuestionOptionEntity> updatedOptions = new ArrayList<>();

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
            }
            
            updatedOptions.add(optionEntity);
        }

        // Limpiar y establecer las opciones actualizadas
        entity.getOptions().clear();
        entity.getOptions().addAll(updatedOptions);
    }
} 