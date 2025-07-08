package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.HomeFeedRepositoryPort;
import pe.com.graduate.insights.api.domain.models.response.HomeFeedResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EventEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.JobOffersEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EventRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.JobOffersRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.HomeFeedMapper;

@Component
@RequiredArgsConstructor
public class HomeFeedRepositoryAdapter implements HomeFeedRepositoryPort {

  private final EventRepository eventRepository;
  private final JobOffersRepository jobOffersRepository;
  private final HomeFeedMapper homeFeedMapper;

  @Override
  public Page<HomeFeedResponse> getHomeFeed(Pageable pageable) {
    // Convertir a HomeFeedResponse usando el mapper
    List<HomeFeedResponse> homeFeedList = new ArrayList<>();
    retriveHome(homeFeedList);

    // Ordenar por fecha de creación descendente
    homeFeedList.sort(Comparator.comparing(HomeFeedResponse::getFechaCreacion).reversed());

    // Aplicar paginación manual
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), homeFeedList.size());

    if (start > homeFeedList.size()) {
      return new PageImpl<>(new ArrayList<>(), pageable, homeFeedList.size());
    }

    List<HomeFeedResponse> pageContent = homeFeedList.subList(start, end);
    return new PageImpl<>(pageContent, pageable, homeFeedList.size());
  }

  @Override
  public List<HomeFeedResponse> getRecentHomeFeed(int limit) {
    // Convertir a HomeFeedResponse usando el mapper
    List<HomeFeedResponse> homeFeedList = new ArrayList<>();
    retriveHome(homeFeedList);
    // Ordenar por fecha de creación descendente y limitar
    return homeFeedList.stream()
        .sorted(Comparator.comparing(HomeFeedResponse::getFechaCreacion).reversed())
        .limit(limit)
        .collect(Collectors.toList());
  }

  private void retriveHome(List<HomeFeedResponse> homeFeedList) {
    // Obtener eventos activos
    List<EventEntity> events = eventRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE);

    // Obtener ofertas laborales activas
    List<JobOffersEntity> jobOffers =
        jobOffersRepository.findAllByEstadoAndEmployer_User_Estado(
            ConstantsUtils.STATUS_ACTIVE, ConstantsUtils.STATUS_ACTIVE);

    // Agregar eventos
    homeFeedList.addAll(events.stream().map(homeFeedMapper::eventToHomeFeed).toList());

    // Agregar ofertas laborales
    homeFeedList.addAll(jobOffers.stream().map(homeFeedMapper::jobOfferToHomeFeed).toList());
  }
}
