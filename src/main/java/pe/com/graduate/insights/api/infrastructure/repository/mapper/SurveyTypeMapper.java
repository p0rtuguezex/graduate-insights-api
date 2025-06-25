package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.SurveyTypeRequest;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyTypeResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyTypeEntity;

@Mapper(componentModel = "spring")
public interface SurveyTypeMapper {

    SurveyTypeMapper INSTANCE = Mappers.getMapper(SurveyTypeMapper.class);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SurveyTypeResponse toDomain(SurveyTypeEntity entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    SurveyTypeEntity toEntity(SurveyTypeRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    void updateEntity(@MappingTarget SurveyTypeEntity entity, SurveyTypeRequest request);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "id", target = "key")
    @Mapping(source = "name", target = "value")
    KeyValueResponse toKeyValueResponse(SurveyTypeEntity surveyTypeEntity);
} 