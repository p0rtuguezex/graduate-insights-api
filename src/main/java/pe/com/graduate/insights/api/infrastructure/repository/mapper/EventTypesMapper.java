package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.EventTypesRequest;
import pe.com.graduate.insights.api.domain.models.response.EventTypesResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EventTypesEntity;

@Mapper(componentModel = "spring")
public interface EventTypesMapper {

  EventTypesMapper INSTANCE = Mappers.getMapper(EventTypesMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "eventTypeId")
  EventTypesResponse toDomain(EventTypesEntity eventTypesEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "estado", constant = "1")
  EventTypesEntity toEntity(EventTypesRequest eventTypesRequest);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "estado", ignore = true)
  void updateEventTypesEntity(
      EventTypesRequest eventTypesRequest, @MappingTarget EventTypesEntity eventTypesEntity);
}
