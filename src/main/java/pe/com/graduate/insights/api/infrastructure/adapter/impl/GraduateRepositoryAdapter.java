package pe.com.graduate.insights.api.infrastructure.adapter.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.GraduateRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.Graduate;

@Slf4j
@Component
@RequiredArgsConstructor
public class GraduateRepositoryAdapter implements GraduateRepositoryPort {

  @Override
  public void save(GraduateRequest request) {}

  @Override
  public List<Graduate> getList(Object... params) {
    return List.of();
  }

  @Override
  public Page<Graduate> getPagination(String search, Pageable pageable) {
    return null;
  }

  @Override
  public Optional<Graduate> getDomain(Long id) {
    return Optional.empty();
  }

  @Override
  public void update(GraduateRequest request, Long id) {}
}
