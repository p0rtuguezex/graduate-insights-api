package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.EventRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.EventException;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.EventRequest;
import pe.com.graduate.insights.api.domain.models.response.EventResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.DirectorEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EventEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EventTypesEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.DirectorRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EventRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EventTypesRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.EventMapper;

@Component
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepositoryPort {

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;
  private final DirectorRepository directorRepository;
  private final EventTypesRepository eventTypesRepository;

  @Override
  public void save(EventRequest request) {
    eventRepository
        .findByNombreAndEstado(request.getNombre(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            eventEntity -> {
              throw new EventException(
                  String.format(ConstantsUtils.EVENT_CONFLICT, request.getNombre()));
            },
            () -> {
              EventEntity eventEntity = eventMapper.toEntity(request);

              // Buscar y asignar el director
              DirectorEntity director =
                  directorRepository
                      .findByIdAndUserEstado(request.getDirectorId(), ConstantsUtils.STATUS_ACTIVE)
                      .orElseThrow(
                          () ->
                              new NotFoundException(
                                  String.format(
                                      ConstantsUtils.DIRECTOR_NOT_FOUND, request.getDirectorId())));

              // Buscar y asignar el tipo de evento
              EventTypesEntity eventType =
                  eventTypesRepository
                      .findByIdAndEstado(request.getEventTypeId(), ConstantsUtils.STATUS_ACTIVE)
                      .orElseThrow(
                          () ->
                              new NotFoundException(
                                  String.format(
                                      ConstantsUtils.EVENT_TYPES_NOT_FOUND_ID,
                                      request.getEventTypeId())));

              eventEntity.setDirector(director);
              eventEntity.setEventType(eventType);
              eventEntity.setContenido(request.getContenido());
              eventEntity.setNombre(request.getNombre());
              eventEntity.setEstado(ConstantsUtils.STATUS_ACTIVE);
              eventRepository.save(eventEntity);
            });
  }

  @Override
  public void delete(Long id) {
    eventRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            eventEntity -> eventRepository.deactivateEvent(id),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.EVENT_NOT_FOUND, id));
            });
  }

  @Override
  public List<KeyValueResponse> getList() {
    return eventRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .map(eventMapper::toKeyValueResponse)
        .toList();
  }

  @Override
  public Page<EventResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<EventEntity> eventEntities =
        hasSearch
            ? eventRepository.findAllByEstadoSearch(search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : eventRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);
    List<EventResponse> eventResponseList =
        eventEntities.getContent().stream().map(eventMapper::toDomain).toList();
    return new PageImpl<>(eventResponseList, pageable, eventEntities.getTotalElements());
  }

  @Override
  public EventResponse getDomain(Long id) {
    return eventRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(eventMapper::toDomain)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.EVENT_NOT_FOUND, id)));
  }

  @Override
  public void update(EventRequest request, Long id) {
    eventRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(
            eventEntity -> {
              eventMapper.updateEventEntity(request, eventEntity);

              // Buscar y asignar el director si cambió
              if (!Objects.equals(request.getDirectorId(), eventEntity.getDirector().getId())) {
                DirectorEntity director =
                    directorRepository
                        .findByIdAndUserEstado(
                            request.getDirectorId(), ConstantsUtils.STATUS_ACTIVE)
                        .orElseThrow(
                            () ->
                                new NotFoundException(
                                    String.format(
                                        ConstantsUtils.DIRECTOR_NOT_FOUND,
                                        request.getDirectorId())));
                eventEntity.setDirector(director);
              }

              // Buscar y asignar el tipo de evento si cambió
              if (!Objects.equals(request.getEventTypeId(), eventEntity.getEventType().getId())) {
                EventTypesEntity eventType =
                    eventTypesRepository
                        .findByIdAndEstado(request.getEventTypeId(), ConstantsUtils.STATUS_ACTIVE)
                        .orElseThrow(
                            () ->
                                new NotFoundException(
                                    String.format(
                                        ConstantsUtils.EVENT_TYPES_NOT_FOUND_ID,
                                        request.getEventTypeId())));
                eventEntity.setEventType(eventType);
              }

              return eventRepository.save(eventEntity);
            })
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.EVENT_NOT_FOUND, id)));
  }
}
