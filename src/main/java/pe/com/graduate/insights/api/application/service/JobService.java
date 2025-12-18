package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.graduate.insights.api.application.ports.input.JobUseCase;
import pe.com.graduate.insights.api.application.ports.output.GraduateRepositoryPort;
import pe.com.graduate.insights.api.application.ports.output.JobRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.JobException;
import pe.com.graduate.insights.api.domain.models.context.UserContext;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService implements JobUseCase {

  private final JobRepositoryPort jobRepositoryPort;
  private final GraduateRepositoryPort graduateRepositoryPort;

  @Override
  public JobResponse getJob(Long id, UserContext userContext) {
    return userContext.isDirector()
        ? jobRepositoryPort.getDomain(id)
        : jobRepositoryPort.getDomainByGraduate(id, resolveGraduateId(userContext));
  }

  @Override
  @Transactional
  public void createJob(JobRequest jobRequest, UserContext userContext) {
    Long graduateId = resolveGraduateId(jobRequest, userContext);
    jobRequest.setGraduateId(graduateId);
    jobRepositoryPort.save(jobRequest);
  }

  @Override
  @Transactional
  public void updateJob(Long id, JobRequest jobRequest, UserContext userContext) {
    if (userContext.isDirector()) {
      requireGraduate(jobRequest);
    } else {
      Long graduateId = resolveGraduateId(userContext);
      jobRepositoryPort.getDomainByGraduate(id, graduateId);
      jobRequest.setGraduateId(graduateId);
    }
    jobRepositoryPort.update(jobRequest, id);
  }

  @Override
  @Transactional
  public void deleteJob(Long id, UserContext userContext) {
    if (userContext.isDirector()) {
      jobRepositoryPort.delete(id);
    } else {
      Long graduateId = resolveGraduateId(userContext);
      jobRepositoryPort.deleteByGraduate(id, graduateId);
    }
  }

  @Override
  public Page<JobResponse> getJobs(String search, Pageable pageable, UserContext userContext) {
    return userContext.isDirector()
        ? jobRepositoryPort.getPagination(search, pageable)
        : jobRepositoryPort.getPaginationByGraduate(
            search, pageable, resolveGraduateId(userContext));
  }

  @Override
  public List<KeyValueResponse> getJobOptions(UserContext userContext) {
    return userContext.isDirector()
        ? jobRepositoryPort.getList()
        : jobRepositoryPort.getListByGraduate(resolveGraduateId(userContext));
  }

  private Long resolveGraduateId(JobRequest jobRequest, UserContext userContext) {
    if (userContext.isDirector()) {
      requireGraduate(jobRequest);
      return jobRequest.getGraduateId();
    }
    return resolveGraduateId(userContext);
  }

  private Long resolveGraduateId(UserContext userContext) {
    return graduateRepositoryPort.getActiveGraduateIdByUserId(userContext.userId());
  }

  private void requireGraduate(JobRequest jobRequest) {
    if (jobRequest.getGraduateId() == null) {
      throw new JobException(ConstantsUtils.JOB_GRADUATE_REQUIRED);
    }
  }
}
