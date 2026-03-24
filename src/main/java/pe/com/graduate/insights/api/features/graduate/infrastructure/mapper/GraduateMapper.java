package pe.com.graduate.insights.api.features.graduate.infrastructure.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateAcademicDegreeResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateLanguageResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateTitulationResponse;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateDegreeEntity;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateLanguageEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateTitulationEntity;
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
    @Mapping(source = "paisResidencia", target = "paisResidencia")
    @Mapping(source = "linkedin", target = "linkedin")
    @Mapping(source = "portafolio", target = "portafolio")
    @Mapping(source = "facultad", target = "facultad")
    @Mapping(source = "escuelaProfesional", target = "escuelaProfesional")
    @Mapping(source = "escuelaProfesionalId", target = "escuelaProfesionalId")
    @Mapping(source = "anioIngreso", target = "anioIngreso")
    @Mapping(source = "anioEgreso", target = "anioEgreso")
    @Mapping(source = "gradoObtenido", target = "gradoObtenido")
    @Mapping(source = "bachillerFecha", target = "bachillerFecha", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "bachillerUniversidad", target = "bachillerUniversidad")
    @Mapping(source = "tituloProfesionalFecha", target = "tituloProfesionalFecha", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "tituloProfesionalUniversidad", target = "tituloProfesionalUniversidad")
    @Mapping(source = "maestriaFecha", target = "maestriaFecha", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "maestriaUniversidad", target = "maestriaUniversidad")
    @Mapping(source = "doctoradoFecha", target = "doctoradoFecha", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "doctoradoUniversidad", target = "doctoradoUniversidad")
    @Mapping(source = "otroGradoNombre", target = "otroGradoNombre")
    @Mapping(source = "otroGradoFecha", target = "otroGradoFecha", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "otroGradoUniversidad", target = "otroGradoUniversidad")
    @Mapping(source = "modalidadTitulacion", target = "modalidadTitulacion")
    @Mapping(source = "modalidadTitulacionOtro", target = "modalidadTitulacionOtro")
    @Mapping(source = "idiomaNombre", target = "idiomaNombre")
    @Mapping(source = "idiomaNivel", target = "idiomaNivel")
    @Mapping(source = "idiomaFechaInicio", target = "idiomaFechaInicio", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "idiomaFechaFin", target = "idiomaFechaFin", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "idiomaAprendizaje", target = "idiomaAprendizaje")
    @Mapping(source = "grados", target = "grados")
    @Mapping(source = "idiomas", target = "idiomas")
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
  @Mapping(source = "id", target = "key")
  @Mapping(source = "user.nombreCompleto", target = "value")
  KeyValueResponse toKeyValueResponse(GraduateEntity graduateEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "user", source = "userEntity")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "grados", ignore = true)
  @Mapping(target = "idiomas", ignore = true)
  GraduateEntity toEntity(GraduateRequest graduateRequest, UserEntity userEntity);

  UserRequest toGraduateRequest(GraduateRequest graduateRequest);

  @BeanMapping(
      unmappedTargetPolicy = ReportingPolicy.IGNORE,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "cv", ignore = true)
  @Mapping(target = "grados", ignore = true)
  @Mapping(target = "idiomas", ignore = true)
  @Mapping(target = "user.nombres", source = "nombres")
  @Mapping(target = "user.apellidos", source = "apellidos")
  @Mapping(target = "user.fechaNacimiento", source = "fechaNacimiento")
  @Mapping(target = "user.genero", source = "genero")
  @Mapping(target = "user.correo", source = "correo")
  @Mapping(target = "user.dni", source = "dni")
  @Mapping(target = "user.celular", source = "celular")
  @Mapping(target = "user.contrasena", source = "contrasena")
  void updateGraduateEntity(
      GraduateRequest graduateRequest, @MappingTarget GraduateEntity graduateEntity);
}





