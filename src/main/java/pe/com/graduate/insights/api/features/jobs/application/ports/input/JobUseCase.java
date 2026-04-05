package pe.com.graduate.insights.api.features.jobs.application.ports.input;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.features.jobs.application.dto.JobRequest;
import pe.com.graduate.insights.api.features.jobs.application.dto.JobResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.shared.security.UserContext;

public interface JobUseCase {

  JobResponse getJob(Long id, UserContext userContext);

  void createJob(JobRequest jobRequest, UserContext userContext);

  void updateJob(Long id, JobRequest jobRequest, UserContext userContext);

  void deleteJob(Long id, UserContext userContext);

  Page<JobResponse> getJobs(String search, Pageable pageable, UserContext userContext);

  List<KeyValueResponse> getJobOptions(UserContext userContext);
}
