package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.GraduateRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;

@Component
@RequiredArgsConstructor
public class GraduateRepositoryAdapter implements GraduateRepositoryPort {

  @Override
  public void save(GraduateRequest request) {}

  @Override
  public List<GraduateResponse> getList(Object... params) {
    return List.of();
  }

  @Override
  public Page<GraduateResponse> getPagination(String search, Pageable pageable) {
    return null;
  }

  @Override
  public GraduateResponse getDomain(Long id) {
    return null;
  }

  @Override
  public void update(GraduateRequest request, Long id) {}
}
