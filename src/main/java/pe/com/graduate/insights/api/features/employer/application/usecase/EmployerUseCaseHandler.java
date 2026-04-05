package pe.com.graduate.insights.api.features.employer.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.employer.application.dto.EmployerRequest;
import pe.com.graduate.insights.api.features.employer.application.dto.EmployerResponse;
import pe.com.graduate.insights.api.features.employer.application.ports.input.EmployerUseCase;
import pe.com.graduate.insights.api.features.employer.application.ports.output.EmployerRepositoryPort;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;

@Service
@RequiredArgsConstructor
public class EmployerUseCaseHandler implements EmployerUseCase {

  private final EmployerRepositoryPort employerRepositoryPort;

  @Override
  public void save(EmployerRequest request) {
    employerRepositoryPort.save(request);
  }

  @Override
  public void delete(Long id) {
    employerRepositoryPort.delete(id);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return employerRepositoryPort.getList();
  }

  @Override
  public Page<EmployerResponse> getPagination(String search, Pageable pageable) {
    return employerRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public EmployerResponse getDomain(Long id) {
    return employerRepositoryPort.getDomain(id);
  }

  @Override
  public void update(EmployerRequest request, Long id) {
    employerRepositoryPort.update(request, id);
  }
}
