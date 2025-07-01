package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.JobUseCase;
import pe.com.graduate.insights.api.application.ports.output.JobRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

@Service
@AllArgsConstructor
public class JobService implements JobUseCase {

  private final JobRepositoryPort jobRepositoryPort;

  @Override
  public JobResponse getDomain(Long id) {
    return jobRepositoryPort.getDomain(id);
  }

  @Override
  public void save(JobRequest request) {
    jobRepositoryPort.save(request);
  }

  @Override
  public void update(JobRequest request, Long id) {
    jobRepositoryPort.update(request, id);
  }

  @Override
  public void delete(Long id) {
    jobRepositoryPort.delete(id);
  }

  @Override
  public Page<JobResponse> getPagination(String search, Pageable pageable) {
    return jobRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return jobRepositoryPort.getList();
  }

  // Implementación de métodos con lógica de roles
  @Override
  public JobResponse getDomainByRole(Long id, boolean isDirector, Long currentUserId) {
    return jobRepositoryPort.getDomainByRole(id, isDirector, currentUserId);
  }

  @Override
  public void saveByRole(JobRequest jobRequest, boolean isDirector, Long currentUserId) {
    jobRepositoryPort.saveByRole(jobRequest, isDirector, currentUserId);
  }

  @Override
  public void updateByRole(JobRequest jobRequest, Long id, boolean isDirector, Long currentUserId) {
    jobRepositoryPort.updateByRole(jobRequest, id, isDirector, currentUserId);
  }

  @Override
  public void deleteByRole(Long id, boolean isDirector, Long currentUserId) {
    jobRepositoryPort.deleteByRole(id, isDirector, currentUserId);
  }

  @Override
  public Page<JobResponse> getPaginationByRole(
      String search, Pageable pageable, boolean isDirector, Long currentUserId) {
    return jobRepositoryPort.getPaginationByRole(search, pageable, isDirector, currentUserId);
  }

  @Override
  public List<KeyValueResponse> getListByRole(boolean isDirector, Long currentUserId) {
    return jobRepositoryPort.getListByRole(isDirector, currentUserId);
  }
}
