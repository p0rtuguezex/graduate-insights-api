package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.EventUseCase;
import pe.com.graduate.insights.api.application.ports.output.EventRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.EventRequest;
import pe.com.graduate.insights.api.domain.models.response.EventResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

@Service
@AllArgsConstructor
public class EventService implements EventUseCase {

  private final EventRepositoryPort eventRepositoryPort;

  @Override
  public void save(EventRequest request) {
    eventRepositoryPort.save(request);
  }

  @Override
  public void delete(Long id) {
    eventRepositoryPort.delete(id);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return eventRepositoryPort.getList();
  }

  @Override
  public Page<EventResponse> getPagination(String search, Pageable pageable) {
    return eventRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public EventResponse getDomain(Long id) {
    return eventRepositoryPort.getDomain(id);
  }

  @Override
  public void update(EventRequest request, Long id) {
    eventRepositoryPort.update(request, id);
  }
}
