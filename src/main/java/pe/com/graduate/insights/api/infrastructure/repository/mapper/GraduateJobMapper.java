package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.GraduateJobRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateJobResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.JobEntity;

@Mapper(componentModel = "spring")
public interface GraduateJobMapper {

  GraduateJobMapper INSTANCE = Mappers.getMapper(GraduateJobMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "graduate", source = "graduateEntity")
  @Mapping(target = "estado", constant = "1")
  @Mapping(source = "graduateJobRequest.compania", target = "compania")
  @Mapping(source = "graduateJobRequest.cargo", target = "cargo")
  @Mapping(source = "graduateJobRequest.modalidad", target = "modalidad")
  @Mapping(source = "graduateJobRequest.fechaInicio", target = "fechaInicio")
  @Mapping(source = "graduateJobRequest.fechaFin", target = "fechaFin")
  @Mapping(target = "id", ignore = true)
  JobEntity toEntity(GraduateJobRequest graduateJobRequest, GraduateEntity graduateEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "jobId")
  GraduateJobResponse toJobResponse(JobEntity jobEntity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "estado", ignore = true)
  @Mapping(target = "graduate", ignore = true)
  void updateJobEntity(GraduateJobRequest graduateJobRequest, @MappingTarget JobEntity jobEntity);
}
