package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.EducationCenterRequest;
import pe.com.graduate.insights.api.domain.models.response.EducationCenterResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EducationCenterEntity;

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
