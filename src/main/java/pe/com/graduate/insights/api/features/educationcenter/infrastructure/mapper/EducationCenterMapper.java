package pe.com.graduate.insights.api.features.educationcenter.infrastructure.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.features.educationcenter.application.dto.EducationCenterRequest;
import pe.com.graduate.insights.api.features.educationcenter.application.dto.EducationCenterResponse;
import pe.com.graduate.insights.api.features.educationcenter.infrastructure.entity.EducationCenterEntity;

@Mapper(componentModel = "spring")
public interface EducationCenterMapper {

  EducationCenterMapper INSTANCE = Mappers.getMapper(EducationCenterMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "educationCenterId")
  EducationCenterResponse toDomain(EducationCenterEntity educationCenterEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "estado", constant = "1")
  EducationCenterEntity toEntity(EducationCenterRequest educationCenterRequest);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "estado", ignore = true)
  void updateEducationCenterEntity(
      EducationCenterRequest educationCenterRequest,
      @MappingTarget EducationCenterEntity educationCenterEntity);
}
