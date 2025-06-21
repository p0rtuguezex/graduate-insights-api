package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.GraduateUseCase;
import pe.com.graduate.insights.api.application.ports.output.GraduateRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

@Service
@AllArgsConstructor
public class GraduateService implements GraduateUseCase {

  private final GraduateRepositoryPort graduateRepositoryPort;

  @Override
  public void save(GraduateRequest request) {
    graduateRepositoryPort.save(request);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return graduateRepositoryPort.getList();
  }

  @Override
  public Page<GraduateResponse> getPagination(String search, Pageable pageable) {
    return graduateRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public GraduateResponse getDomain(Long id) {
    return graduateRepositoryPort.getDomain(id);
  }

  @Override
  public void update(GraduateRequest request, Long id) {
    graduateRepositoryPort.update(request, id);
  }

  @Override
  public void delete(Long id) {
    graduateRepositoryPort.delete(id);
  }
}
