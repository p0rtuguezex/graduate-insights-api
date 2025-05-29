package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.DirectorRequest;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.DirectorResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.DirectorEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface DirectorMapper {

  DirectorMapper INSTANCE = Mappers.getMapper(DirectorMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "directorId")
  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "user.nombres", target = "nombres")
  @Mapping(source = "user.apellidos", target = "apellidos")
  @Mapping(source = "user.fechaNacimiento", target = "fechaNacimiento")
  @Mapping(source = "user.genero", target = "genero")
  @Mapping(source = "user.correo", target = "correo")
  @Mapping(source = "user.estado", target = "estado")
  @Mapping(source = "user.dni", target = "dni")
  @Mapping(source = "user.celular", target = "celular")
  DirectorResponse toDomain(DirectorEntity directorEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "user", source = "userEntity")
  @Mapping(target = "id", ignore = true)
  DirectorEntity toEntity(DirectorRequest directorRequest, UserEntity userEntity);

  UserRequest toDirectorRequest(DirectorRequest directorRequest);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "user.nombres", source = "nombres")
  @Mapping(target = "user.apellidos", source = "apellidos")
  @Mapping(target = "user.fechaNacimiento", source = "fechaNacimiento")
  @Mapping(target = "user.genero", source = "genero")
  @Mapping(target = "user.correo", source = "correo")
  @Mapping(target = "user.dni", source = "dni")
  @Mapping(target = "user.celular", source = "celular")
  @Mapping(target = "user.contrasena", source = "contrasena")
  void updateDirectorEntity(
      DirectorRequest directorRequest, @MappingTarget DirectorEntity directorEntity);
}
