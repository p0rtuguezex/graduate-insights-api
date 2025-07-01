package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.JobOffersRequest;
import pe.com.graduate.insights.api.domain.models.response.JobOffersResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EmployerEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.JobOffersEntity;

@Mapper(componentModel = "spring")
public interface JobOffersMapper {

  JobOffersMapper INSTANCE = Mappers.getMapper(JobOffersMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "employer", source = "employerEntity")
  @Mapping(target = "estado", constant = "1")
  @Mapping(source = "jobOffersRequest.titulo", target = "titulo")
  @Mapping(source = "jobOffersRequest.link", target = "link")
  @Mapping(source = "jobOffersRequest.descripcion", target = "descripcion")
  @Mapping(target = "id", ignore = true)
  JobOffersEntity toEntity(JobOffersRequest jobOffersRequest, EmployerEntity employerEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "key")
  @Mapping(source = "titulo", target = "value")
  KeyValueResponse toKeyValueResponse(JobOffersEntity jobOffersEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "jobOffersId")
  @Mapping(source = "employer.id", target = "employerId")
  @Mapping(source = "employer.razonSocial", target = "employerName")
  JobOffersResponse toJobOffersResponse(JobOffersEntity jobOffersEntity);

  /**
   * Método especial para employers que no deben ver su propio employer_id
   */
  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "jobOffersId")
  @Mapping(target = "employerId", ignore = true)
  @Mapping(source = "employer.razonSocial", target = "employerName")
  JobOffersResponse toJobOffersResponseWithoutEmployerId(JobOffersEntity jobOffersEntity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "estado", ignore = true)
  @Mapping(target = "employer", ignore = true)
  void updateJobOffersEntity(
      JobOffersRequest jobOffersRequest, @MappingTarget JobOffersEntity jobOffersEntity);
}
