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
  public void save(JobRequest request) {
    jobRepositoryPort.save(request);
  }

  @Override
  public void delete(Long graduateId, Long jobId) {
    jobRepositoryPort.delete(graduateId, jobId);
  }

  @Override
  public List<JobResponse> getJobsList(Long graduateId) {
    return jobRepositoryPort.getJobsList(graduateId);
  }

  @Override
  public Page<JobResponse> getPagination(String search, Pageable pageable, Long graduateId) {
    return jobRepositoryPort.getPagination(search, pageable, graduateId);
  }

  @Override
  public List<KeyValueResponse> getJobList() {
    return jobRepositoryPort.getJobList();
  }

  @Override
  public JobResponse getDomain(Long graduateId, Long jobId) {
    return jobRepositoryPort.getDomain(graduateId, jobId);
  }

  @Override
  public void update(JobRequest request, Long jobId) {
    jobRepositoryPort.update(request, jobId);
  }
}
