package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.EducationCenterUseCase;
import pe.com.graduate.insights.api.application.ports.output.EducationCenterRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.EducationCenterRequest;
import pe.com.graduate.insights.api.domain.models.response.EducationCenterResponse;

@Service
@AllArgsConstructor
public class EducationCenterService implements EducationCenterUseCase {

  private final EducationCenterRepositoryPort educationCenterRepositoryPort;

  @Override
  public void save(EducationCenterRequest request) {
    educationCenterRepositoryPort.save(request);
  }

  @Override
  public void delete(Long id) {
    educationCenterRepositoryPort.delete(id);
  }

  @Override
  public List<EducationCenterResponse> getList() {
    return educationCenterRepositoryPort.getList();
  }

  @Override
  public Page<EducationCenterResponse> getPagination(String search, Pageable pageable) {
    return educationCenterRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public EducationCenterResponse getDomain(Long id) {
    return educationCenterRepositoryPort.getDomain(id);
  }

  @Override
  public void update(EducationCenterRequest request, Long id) {
    educationCenterRepositoryPort.update(request, id);
  }
}
