package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.EventRequest;
import pe.com.graduate.insights.api.domain.models.response.EventResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EventEntity;

@Mapper(componentModel = "spring")
public interface EventMapper {

  EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "eventId")
  @Mapping(source = "director.id", target = "directorId")
  @Mapping(source = "director.user.nombres", target = "directorNombre")
  @Mapping(source = "eventType.id", target = "eventTypeId")
  @Mapping(source = "eventType.nombre", target = "eventTypeNombre")
  EventResponse toDomain(EventEntity eventEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "key")
  @Mapping(source = "nombre", target = "value")
  KeyValueResponse toKeyValueResponse(EventEntity eventEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "estado", constant = "1")
  @Mapping(target = "director", ignore = true)
  @Mapping(target = "eventType", ignore = true)
  EventEntity toEntity(EventRequest eventRequest);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "estado", ignore = true)
  @Mapping(target = "director", ignore = true)
  @Mapping(target = "eventType", ignore = true)
  void updateEventEntity(EventRequest eventRequest, @MappingTarget EventEntity eventEntity);
}
