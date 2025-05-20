package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface GraduateMapper {

  GraduateMapper INSTANCE = Mappers.getMapper(GraduateMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  GraduateResponse toDomain(GraduateEntity graduateEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  GraduateEntity toEntity(GraduateRequest graduateRequest);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "user", source = "userEntity")
  @Mapping(target = "id", ignore = true)
  GraduateEntity toEntity(GraduateRequest graduateRequest, UserEntity userEntity);

  UserRequest toGraduateRequest(GraduateRequest graduateRequest);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "user", ignore = true)
  void updateGraduateEntity(
      GraduateRequest graduateRequest, @MappingTarget GraduateEntity graduateEntity);
}
