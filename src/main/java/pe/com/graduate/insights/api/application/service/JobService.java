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

@Service
@AllArgsConstructor
public class JobService implements JobUseCase {

  private final JobRepositoryPort jobRepositoryPort;

  @Override
  public void save(JobRequest request) {
    jobRepositoryPort.save(request);
  }

  @Override
  public void delete(Long id) {
    jobRepositoryPort.delete(id);
  }

  @Override
  public List<JobResponse> getList() {
    return jobRepositoryPort.getList();
  }

  @Override
  public Page<JobResponse> getPagination(String search, Pageable pageable) {
    return jobRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public JobResponse getDomain(Long id) {
    return jobRepositoryPort.getDomain(id);
  }

  @Override
  public void update(JobRequest request, Long id) {
    jobRepositoryPort.update(request, id);
  }
} 