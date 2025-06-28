package pe.com.graduate.insights.api.application.ports.input;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

public interface JobUseCase {
  List<JobResponse> getJobsList(Long graduateId);

  JobResponse getDomain(Long graduateId, Long jobId);

  void save(JobRequest jobRequest);

  void update(JobRequest jobRequest, Long jobId);

  void delete(Long graduateId, Long jobId);

  Page<JobResponse> getPagination(String search, Pageable pageable, Long graduateId);

  List<KeyValueResponse> getJobList();
}
