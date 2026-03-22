package pe.com.graduate.insights.api.features.eventtypes.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.features.eventtypes.application.dto.EventTypesRequest;
import pe.com.graduate.insights.api.features.eventtypes.application.dto.EventTypesResponse;
import pe.com.graduate.insights.api.features.eventtypes.application.ports.input.EventTypesUseCase;
import pe.com.graduate.insights.api.features.eventtypes.application.ports.output.EventTypesRepositoryPort;

@Service
@RequiredArgsConstructor
public class EventTypesUseCaseHandler implements EventTypesUseCase {

  private final EventTypesRepositoryPort eventTypesRepositoryPort;

  @Override
  public void save(EventTypesRequest request) {
    eventTypesRepositoryPort.save(request);
  }

  @Override
  public void delete(Long id) {
    eventTypesRepositoryPort.delete(id);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return eventTypesRepositoryPort.getList();
  }

  @Override
  public Page<EventTypesResponse> getPagination(String search, Pageable pageable) {
    return eventTypesRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public EventTypesResponse getDomain(Long id) {
    return eventTypesRepositoryPort.getDomain(id);
  }

  @Override
  public void update(EventTypesRequest request, Long id) {
    eventTypesRepositoryPort.update(request, id);
  }
}
