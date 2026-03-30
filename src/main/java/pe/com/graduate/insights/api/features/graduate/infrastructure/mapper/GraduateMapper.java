package pe.com.graduate.insights.api.features.graduate.infrastructure.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateAcademicDegreeResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateComplementaryTrainingResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateLanguageResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateTitulationResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateWorkTrajectoryResponse;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateComplementaryTrainingEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateDegreeEntity;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateLanguageEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateTitulationEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateWorkTrajectoryEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface GraduateMapper {

  GraduateMapper INSTANCE = Mappers.getMapper(GraduateMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "graduateId")
  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "user.nombres", target = "nombres")
  @Mapping(source = "user.apellidos", target = "apellidos")
  @Mapping(source = "user.fechaNacimiento", target = "fechaNacimiento")
  @Mapping(source = "user.genero", target = "genero")
  @Mapping(source = "user.correo", target = "correo")
  @Mapping(source = "user.estado", target = "estado")
  @Mapping(source = "user.dni", target = "dni")
  @Mapping(source = "user.celular", target = "celular")
    @Mapping(source = "codigoUniversitario", target = "codigoUniversitario")
    @Mapping(source = "estadoCivil", target = "estadoCivil")
    @Mapping(source = "nacionalidad", target = "nacionalidad")
    @Mapping(source = "correoInstitucional", target = "correoInstitucional")
    @Mapping(source = "direccionActual", target = "direccionActual")
    @Mapping(source = "ciudad", target = "ciudad")
    @Mapping(source = "departamento", target = "departamento")
    @Mapping(source = "provincia", target = "provincia")
    @Mapping(source = "distrito", target = "distrito")
    @Mapping(source = "paisResidencia", target = "paisResidencia")
    @Mapping(source = "viveEnPeru", target = "viveEnPeru")
    @Mapping(source = "linkedin", target = "linkedin")
    @Mapping(source = "portafolio", target = "portafolio")
    @Mapping(source = "escuelaProfesionalId", target = "escuelaProfesionalId")
    @Mapping(source = "anioIngreso", target = "anioIngreso")
    @Mapping(source = "anioEgreso", target = "anioEgreso")
    @Mapping(source = "grados", target = "grados")
    @Mapping(source = "idiomas", target = "idiomas")
    @Mapping(source = "formacionesComplementarias", target = "formacionesComplementarias")
    @Mapping(source = "trayectoriasLaborales", target = "trayectoriasLaborales")
  @Mapping(source = "fotoPath", target = "fotoPath")
  @Mapping(source = "validated", target = "validated")
  GraduateResponse toDomain(GraduateEntity graduateEntity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "tipoGradoId", target = "tipoGradoId")
    @Mapping(source = "universidadId", target = "universidadId")
    @Mapping(source = "fechaGrado", target = "fechaGrado", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "otroGradoNombre", target = "otroGradoNombre")
    @Mapping(source = "titulation", target = "titulacion")
    GraduateAcademicDegreeResponse toDegreeResponse(GraduateDegreeEntity entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "modalidadTitulacionId", target = "modalidadTitulacionId")
    @Mapping(source = "modalidadOtro", target = "modalidadOtro")
    GraduateTitulationResponse toTitulationResponse(GraduateTitulationEntity entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "idiomaId", target = "idiomaId")
    @Mapping(source = "nivel", target = "nivel")
    @Mapping(source = "fechaInicio", target = "fechaInicio", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "fechaFin", target = "fechaFin", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "aprendizaje", target = "aprendizaje")
    GraduateLanguageResponse toLanguageResponse(GraduateLanguageEntity entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "nombreCurso", target = "nombreCurso")
    @Mapping(source = "institucion", target = "institucion")
    @Mapping(source = "fechaInicio", target = "fechaInicio", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "fechaFin", target = "fechaFin", dateFormat = "yyyy-MM-dd")
    GraduateComplementaryTrainingResponse toComplementaryTrainingResponse(
      GraduateComplementaryTrainingEntity entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "empresa", target = "empresa")
    @Mapping(source = "cargo", target = "cargo")
    @Mapping(source = "modalidad", target = "modalidad")
    @Mapping(source = "fechaInicio", target = "fechaInicio", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "fechaFin", target = "fechaFin", dateFormat = "yyyy-MM-dd")
    GraduateWorkTrajectoryResponse toWorkTrajectoryResponse(GraduateWorkTrajectoryEntity entity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "key")
  @Mapping(source = "user.nombreCompleto", target = "value")
  KeyValueResponse toKeyValueResponse(GraduateEntity graduateEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "user", source = "userEntity")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "grados", ignore = true)
  @Mapping(target = "idiomas", ignore = true)
  @Mapping(target = "formacionesComplementarias", ignore = true)
  @Mapping(target = "trayectoriasLaborales", ignore = true)
  GraduateEntity toEntity(GraduateRequest graduateRequest, UserEntity userEntity);

  UserRequest toGraduateRequest(GraduateRequest graduateRequest);

  @BeanMapping(
      unmappedTargetPolicy = ReportingPolicy.IGNORE,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "grados", ignore = true)
  @Mapping(target = "idiomas", ignore = true)
  @Mapping(target = "formacionesComplementarias", ignore = true)
  @Mapping(target = "trayectoriasLaborales", ignore = true)
  @Mapping(target = "user.nombres", source = "nombres")
  @Mapping(target = "user.apellidos", source = "apellidos")
  @Mapping(target = "user.fechaNacimiento", source = "fechaNacimiento")
  @Mapping(target = "user.genero", source = "genero")
  @Mapping(target = "user.correo", source = "correo")
  @Mapping(target = "user.dni", source = "dni")
  @Mapping(target = "user.celular", source = "celular")
  @Mapping(target = "user.contrasena", source = "contrasena")
  @Mapping(target = "fotoPath", source = "fotoPath")
  void updateGraduateEntity(
      GraduateRequest graduateRequest, @MappingTarget GraduateEntity graduateEntity);
}
