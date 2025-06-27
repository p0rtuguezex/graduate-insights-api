package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.JobRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.JobEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.JobRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.JobMapper;

@Component
@RequiredArgsConstructor
public class JobRepositoryAdapter implements JobRepositoryPort {

  private final JobRepository jobRepository;
  private final GraduateRepository graduateRepository;
  private final JobMapper jobMapper;
  private final ValidationUtilsAdapter validationUtilsAdapter;

  @Override
  public List<JobResponse> getJobsList(Long graduateId) {
    var graduate = validationUtilsAdapter.getAuthenticatedGraduateId(graduateId);
    return Optional.of(
            jobRepository.findAllByEstadoAndGraduateId(
                ConstantsUtils.STATUS_ACTIVE, graduate.getId()))
        .filter(jobs -> !jobs.isEmpty())
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(
                        ConstantsUtils.JOB_OFFERS_NOT_FOUND_BY_EMPLOYER, graduate.getId())))
        .stream()
        .map(jobMapper::toJobResponse)
        .collect(Collectors.toList());
  }

  @Override
  public JobResponse getDomain(Long graduateId, Long jobId) {
    var graduate = validationUtilsAdapter.getAuthenticatedGraduateId(graduateId);
    return jobRepository
        .findByIdAndEstadoAndGraduateId(jobId, ConstantsUtils.STATUS_ACTIVE, graduate.getId())
        .map(jobMapper::toJobResponse)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, jobId)));
  }

  @Override
  public void save(JobRequest jobRequest) {
    var graduate = validationUtilsAdapter.getAuthenticatedGraduateId(jobRequest.getGraduateId());
    JobEntity jobEntity = jobMapper.toEntity(jobRequest, graduate);
    jobRepository.save(jobEntity);
  }

  @Override
  public void update(JobRequest jobRequest, Long jobId) {
    var graduate = validationUtilsAdapter.getAuthenticatedGraduateId(jobRequest.getGraduateId());
    JobEntity jobEntity =
        jobRepository
            .findByIdAndEstadoAndGraduateId(jobId, ConstantsUtils.STATUS_ACTIVE, graduate.getId())
            .orElseThrow(
                () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, jobId)));

    // Verificar si se está cambiando el graduado
    if (!jobEntity.getGraduate().getId().equals(jobRequest.getGraduateId())) {
      graduateRepository
          .findByIdAndUserEstado(jobRequest.getGraduateId(), ConstantsUtils.STATUS_ACTIVE)
          .ifPresentOrElse(
              newGraduate -> {
                jobEntity.setGraduate(newGraduate);
                jobMapper.updateJobEntity(jobRequest, jobEntity);
                jobRepository.save(jobEntity);
              },
              () -> {
                throw new NotFoundException(
                    String.format(ConstantsUtils.GRADUATE_NOT_FOUND, jobRequest.getGraduateId()));
              });
    } else {
      jobMapper.updateJobEntity(jobRequest, jobEntity);
      jobRepository.save(jobEntity);
    }
  }

  @Override
  public void delete(Long graduateId, Long jobId) {
    var graduate = validationUtilsAdapter.getAuthenticatedGraduateId(graduateId);
    jobRepository
        .findByIdAndEstadoAndGraduateId(jobId, ConstantsUtils.STATUS_ACTIVE, graduate.getId())
        .ifPresentOrElse(
            jobEntity -> jobRepository.deactivateJob(jobId),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, jobId));
            });
  }

  @Override
  public Page<JobResponse> getPagination(String search, Pageable pageable, Long graduateId) {
    var graduate = validationUtilsAdapter.getAuthenticatedGraduateId(graduateId);
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<JobEntity> jobEntities =
        hasSearch
            ? jobRepository.findAllByEstadoAndSearchAndGraduateId(
                search, ConstantsUtils.STATUS_ACTIVE, pageable, graduate.getId())
            : jobRepository.findAllByEstadoAndGraduateId(
                ConstantsUtils.STATUS_ACTIVE, pageable, graduate.getId());

    List<JobResponse> jobResponseList =
        jobEntities.getContent().stream().map(jobMapper::toJobResponse).toList();
    return new PageImpl<>(jobResponseList, pageable, jobEntities.getTotalElements());
  }
}
