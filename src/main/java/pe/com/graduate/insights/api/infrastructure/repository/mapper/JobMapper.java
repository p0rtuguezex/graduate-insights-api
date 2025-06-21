package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.JobEntity;

@Mapper(componentModel = "spring")
public interface JobMapper {

  JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "graduate", source = "graduateEntity")
  @Mapping(target = "estado", constant = "1")
  @Mapping(source = "jobRequest.compania", target = "compania")
  @Mapping(source = "jobRequest.cargo", target = "cargo")
  @Mapping(source = "jobRequest.modalidad", target = "modalidad")
  @Mapping(source = "jobRequest.fechaInicio", target = "fechaInicio")
  @Mapping(source = "jobRequest.fechaFin", target = "fechaFin")
  @Mapping(target = "id", ignore = true)
  JobEntity toEntity(JobRequest jobRequest, GraduateEntity graduateEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "jobId")
  @Mapping(source = "graduate.id", target = "graduateId")
  @Mapping(source = "graduate.user.nombres", target = "graduateName")
  JobResponse toJobResponse(JobEntity jobEntity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "estado", ignore = true)
  @Mapping(target = "graduate", ignore = true)
  void updateJobEntity(JobRequest jobRequest, @MappingTarget JobEntity jobEntity);
} 