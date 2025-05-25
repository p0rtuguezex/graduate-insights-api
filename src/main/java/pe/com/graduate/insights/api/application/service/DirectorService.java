package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.DirectorUseCase;
import pe.com.graduate.insights.api.application.ports.output.DirectorRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.DirectorRequest;
import pe.com.graduate.insights.api.domain.models.response.DirectorResponse;

@Service
@RequiredArgsConstructor
public class DirectorService implements DirectorUseCase {

  private final DirectorRepositoryPort directorRepositoryPort;

  @Override
  public void save(DirectorRequest request) {
    directorRepositoryPort.save(request);
  }

  @Override
  public void delete(Long id) {
    directorRepositoryPort.delete(id);
  }

  @Override
  public List<DirectorResponse> getList() {
    return directorRepositoryPort.getList();
  }

  @Override
  public Page<DirectorResponse> getPagination(String search, Pageable pageable) {
    return directorRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public DirectorResponse getDomain(Long id) {
    return directorRepositoryPort.getDomain(id);
  }

  @Override
  public void update(DirectorRequest request, Long id) {
    directorRepositoryPort.update(request, id);
  }
}
