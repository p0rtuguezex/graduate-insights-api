package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.domain.models.request.GraduateQuestionResponseRequest;
import pe.com.graduate.insights.api.infrastructure.repository.entities.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(
    componentModel = "spring",
    imports = {
        LocalDateTime.class
    }
)
public interface GraduateSurveyResponseMapper {

    GraduateSurveyResponseMapper INSTANCE = Mappers.getMapper(GraduateSurveyResponseMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "survey.id", source = "surveyId")
    @Mapping(target = "graduate", ignore = true)
    @Mapping(target = "submittedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "completed", constant = "true")
    @Mapping(target = "createdDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedDate", expression = "java(LocalDateTime.now())")
    GraduateSurveyResponseEntity toEntity(GraduateSurveyResponseRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question.id", source = "questionId")
    @Mapping(target = "selectedOptions", ignore = true)
    @Mapping(target = "createdDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "graduateSurveyResponse", ignore = true)
    GraduateQuestionResponseEntity toEntity(GraduateQuestionResponseRequest request);

    List<GraduateQuestionResponseEntity> toEntityList(List<GraduateQuestionResponseRequest> requests);

    @AfterMapping
    default void linkQuestionResponseRelations(GraduateSurveyResponseRequest request, @MappingTarget GraduateSurveyResponseEntity response) {
        if (response.getResponses() != null) {
            response.getResponses().forEach(questionResponse -> {
                questionResponse.setGraduateSurveyResponse(response);
            });
        }
    }
} 