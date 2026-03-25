package pe.com.graduate.insights.api.features.employer.infrastructure.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.features.employer.application.dto.EmployerRequest;
import pe.com.graduate.insights.api.features.employer.application.dto.EmployerResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.features.employer.infrastructure.entity.EmployerEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface EmployerMapper {

  EmployerMapper INSTANCE = Mappers.getMapper(EmployerMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "employerId")
  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "user.nombres", target = "nombres")
  @Mapping(source = "user.apellidos", target = "apellidos")
  @Mapping(source = "user.fechaNacimiento", target = "fechaNacimiento")
  @Mapping(source = "user.genero", target = "genero")
  @Mapping(source = "user.correo", target = "correo")
  @Mapping(source = "user.estado", target = "estado")
  @Mapping(source = "user.dni", target = "dni")
  @Mapping(source = "user.celular", target = "celular")
  EmployerResponse toDomain(EmployerEntity employerEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "key")
  @Mapping(source = "user.nombreCompleto", target = "value")
  KeyValueResponse toKeyValueResponse(EmployerEntity graduateEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "user", source = "userEntity")
  @Mapping(target = "id", ignore = true)
  EmployerEntity toEntity(EmployerRequest employerRequest, UserEntity userEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "user", ignore = true)
  void updateEmployerEntity(
      EmployerRequest employerRequest, @MappingTarget EmployerEntity employerEntity);
}
