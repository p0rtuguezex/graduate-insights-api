package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.EventTypesRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.EventTypesException;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.EventTypesRequest;
import pe.com.graduate.insights.api.domain.models.response.EventTypesResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EventTypesEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EventTypesRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.EventTypesMapper;

@Component
@RequiredArgsConstructor
public class EventTypesRepositoryAdapter implements EventTypesRepositoryPort {

  private final EventTypesRepository eventTypesRepository;
  private final EventTypesMapper eventTypesMapper;

  @Override
  public void save(EventTypesRequest request) {
    eventTypesRepository
        .findByNombreAndEstado(request.getNombre(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            educationCenterEntity -> {
              throw new EventTypesException(
                  String.format(ConstantsUtils.EVENT_TYPES_CENTER_CONFLICT, request.getNombre()));
            },
            () -> {
              EventTypesEntity eventTypesEntity = eventTypesMapper.toEntity(request);
              eventTypesRepository.save(eventTypesEntity);
            });
  }

  @Override
  public void delete(Long id) {
    eventTypesRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            educationCenterEntity -> eventTypesRepository.deactivateEventType(id),
            () -> {
              throw new NotFoundException(
                  String.format(ConstantsUtils.EVENT_TYPES_NOT_FOUND_ID, id));
            });
  }

  @Override
  public List<KeyValueResponse> getList() {
    return eventTypesRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .map(eventTypesMapper::toKeyValueResponse)
        .toList();
  }

  @Override
  public Page<EventTypesResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<EventTypesEntity> educationCenterEntities =
        hasSearch
            ? eventTypesRepository.findAllByEstadoSearch(
                search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : eventTypesRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);
    List<EventTypesResponse> eventTypesResponseList =
        educationCenterEntities.getContent().stream().map(eventTypesMapper::toDomain).toList();
    return new PageImpl<>(
        eventTypesResponseList, pageable, educationCenterEntities.getTotalElements());
  }

  @Override
  public EventTypesResponse getDomain(Long id) {
    return eventTypesRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(eventTypesMapper::toDomain)
        .orElseThrow(
            () ->
                new NotFoundException(String.format(ConstantsUtils.EVENT_TYPES_NOT_FOUND_ID, id)));
  }

  @Override
  public void update(EventTypesRequest request, Long id) {
    eventTypesRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(
            educationCenterEntity -> {
              eventTypesMapper.updateEventTypesEntity(request, educationCenterEntity);
              return eventTypesRepository.save(educationCenterEntity);
            })
        .orElseThrow(
            () ->
                new NotFoundException(String.format(ConstantsUtils.EVENT_TYPES_NOT_FOUND_ID, id)));
  }
}
