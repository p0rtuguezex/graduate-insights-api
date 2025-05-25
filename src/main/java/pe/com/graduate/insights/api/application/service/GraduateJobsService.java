package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.GraduateJobsUseCase;
import pe.com.graduate.insights.api.application.ports.output.GraduateJobsRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.GraduateJobRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateJobResponse;

@Service
@AllArgsConstructor
public class GraduateJobsService implements GraduateJobsUseCase {

  private final GraduateJobsRepositoryPort graduateJobsRepositoryPort;

  @Override
  public void save(GraduateJobRequest request, Long graduateId) {
    graduateJobsRepositoryPort.save(request, graduateId);
  }

  @Override
  public Page<GraduateJobResponse> getPagination(
      String search, Pageable pageable, Long graduateId) {
    return graduateJobsRepositoryPort.getPagination(search, pageable, graduateId);
  }

  @Override
  public List<GraduateJobResponse> getJobsList(Long graduateId) {
    return graduateJobsRepositoryPort.getJobsList(graduateId);
  }

  @Override
  public GraduateJobResponse getDomain(Long graduateId, Long jobId) {
    return graduateJobsRepositoryPort.getDomain(graduateId, jobId);
  }

  @Override
  public void update(GraduateJobRequest graduateJobRequest, Long graduateId, Long jobId) {
    graduateJobsRepositoryPort.update(graduateJobRequest, graduateId, jobId);
  }

  @Override
  public void delete(Long graduateId, Long jobId) {
    graduateJobsRepositoryPort.delete(graduateId, jobId);
  }
}
