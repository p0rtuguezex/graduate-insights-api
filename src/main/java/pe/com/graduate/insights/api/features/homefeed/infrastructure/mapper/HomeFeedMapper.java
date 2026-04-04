package pe.com.graduate.insights.api.features.homefeed.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.features.homefeed.application.dto.HomeFeedResponse;
import pe.com.graduate.insights.api.features.event.infrastructure.entity.EventEntity;
import pe.com.graduate.insights.api.features.joboffers.infrastructure.entity.JobOffersEntity;

@Mapper(componentModel = "spring")
public interface HomeFeedMapper {

  HomeFeedMapper INSTANCE = Mappers.getMapper(HomeFeedMapper.class);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "nombre", target = "titulo")
  @Mapping(expression = "java(\"EVENT\")", target = "tipo")
  @Mapping(expression = "java(\"Evento: \" + event.getNombre())", target = "descripcion")
  @Mapping(source = "contenido", target = "contenido")
  @Mapping(source = "createdDate", target = "fechaCreacion")
  @Mapping(
      expression =
          "java(event.getDirector() != null ? event.getDirector().getUser().getNombres() + \" \" + event.getDirector().getUser().getApellidos() : \"Director\")",
      target = "fuente")
  @Mapping(source = "estado", target = "estado")
  @Mapping(source = "eventType.nombre", target = "tipoEvento")
  @Mapping(expression = "java(null)", target = "empresa")
  @Mapping(expression = "java(null)", target = "salario")
  @Mapping(expression = "java(null)", target = "ubicacion")
  @Mapping(expression = "java(null)", target = "link")
  @Mapping(source = "enlaceInscripcion", target = "enlaceInscripcion")
  HomeFeedResponse eventToHomeFeed(EventEntity event);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "titulo", target = "titulo")
  @Mapping(expression = "java(\"JOB_OFFER\")", target = "tipo")
  @Mapping(expression = "java(\"Oferta laboral: \" + jobOffer.getTitulo())", target = "descripcion")
  @Mapping(source = "descripcion", target = "contenido")
  @Mapping(source = "createdDate", target = "fechaCreacion")
  @Mapping(
      expression =
          "java(jobOffer.getEmployer() != null ? jobOffer.getEmployer().getUser().getNombres() + \" \" + jobOffer.getEmployer().getUser().getApellidos() : \"Empleador\")",
      target = "fuente")
  @Mapping(source = "estado", target = "estado")
  @Mapping(expression = "java(null)", target = "tipoEvento")
  @Mapping(source = "employer.razonSocial", target = "empresa")
  @Mapping(expression = "java(null)", target = "salario")
  @Mapping(expression = "java(null)", target = "ubicacion")
  @Mapping(source = "link", target = "link")
  @Mapping(expression = "java(null)", target = "enlaceInscripcion")
  HomeFeedResponse jobOfferToHomeFeed(JobOffersEntity jobOffer);
}




