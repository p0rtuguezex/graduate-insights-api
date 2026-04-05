package pe.com.graduate.insights.api.features.jobs.infrastructure.adapter;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.features.jobs.application.dto.JobRequest;
import pe.com.graduate.insights.api.features.jobs.application.dto.JobResponse;
import pe.com.graduate.insights.api.features.jobs.application.ports.output.JobRepositoryPort;
import pe.com.graduate.insights.api.features.jobs.infrastructure.entity.JobEntity;
import pe.com.graduate.insights.api.features.jobs.infrastructure.jpa.JobRepository;
import pe.com.graduate.insights.api.features.jobs.infrastructure.mapper.JobMapper;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;

@Component
@RequiredArgsConstructor
public class JobRepositoryAdapter implements JobRepositoryPort {

  private final JobRepository jobRepository;
  private final GraduateRepository graduateRepository;
  private final JobMapper jobMapper;

  @Override
  public JobResponse getDomain(Long id) {
    return jobRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(jobMapper::toJobResponse)
        .orElseThrow(() -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id)));
  }

  @Override
  public JobResponse getDomainByGraduate(Long id, Long graduateId) {
    return jobRepository
        .findByIdAndEstadoAndGraduateId(id, ConstantsUtils.STATUS_ACTIVE, graduateId)
        .map(jobMapper::toJobResponseWithoutGraduateId)
        .orElseThrow(() -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id)));
  }

  @Override
  public void save(JobRequest jobRequest) {
    var graduate =
        graduateRepository
            .findByIdAndUserEstado(jobRequest.getGraduateId(), ConstantsUtils.STATUS_ACTIVE)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        String.format(
                            ConstantsUtils.GRADUATE_NOT_FOUND, jobRequest.getGraduateId())));
    JobEntity jobEntity = jobMapper.toEntity(jobRequest, graduate);
    jobRepository.save(jobEntity);
  }

  @Override
  public void update(JobRequest jobRequest, Long id) {
    JobEntity jobEntity =
        jobRepository
            .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
            .orElseThrow(
                () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id)));

    if (!jobEntity.getGraduate().getId().equals(jobRequest.getGraduateId())) {
      var newGraduate =
          graduateRepository
              .findByIdAndUserEstado(jobRequest.getGraduateId(), ConstantsUtils.STATUS_ACTIVE)
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          String.format(
                              ConstantsUtils.GRADUATE_NOT_FOUND, jobRequest.getGraduateId())));
      jobEntity.setGraduate(newGraduate);
    }

    jobMapper.updateJobEntity(jobRequest, jobEntity);
    jobRepository.save(jobEntity);
  }

  @Override
  public void delete(Long id) {
    jobRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            jobEntity -> jobRepository.deactivateJob(id),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id));
            });
  }

  @Override
  public void deleteByGraduate(Long id, Long graduateId) {
    jobRepository
        .findByIdAndEstadoAndGraduateId(id, ConstantsUtils.STATUS_ACTIVE, graduateId)
        .ifPresentOrElse(
            jobEntity -> jobRepository.deactivateJob(id),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id));
            });
  }

  @Override
  public Page<JobResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<JobEntity> jobEntities =
        hasSearch
            ? jobRepository.findAllByEstadoAndSearch(search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : jobRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);

    List<JobResponse> jobResponseList =
        jobEntities.getContent().stream().map(jobMapper::toJobResponse).toList();
    return new PageImpl<>(jobResponseList, pageable, jobEntities.getTotalElements());
  }

  @Override
  public Page<JobResponse> getPaginationByGraduate(
      String search, Pageable pageable, Long graduateId) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<JobEntity> jobEntities =
        hasSearch
            ? jobRepository.findAllByEstadoAndSearchAndGraduateId(
                search, ConstantsUtils.STATUS_ACTIVE, pageable, graduateId)
            : jobRepository.findAllByEstadoAndGraduateId(
                ConstantsUtils.STATUS_ACTIVE, pageable, graduateId);

    List<JobResponse> jobResponseList =
        jobEntities.getContent().stream().map(jobMapper::toJobResponseWithoutGraduateId).toList();
    return new PageImpl<>(jobResponseList, pageable, jobEntities.getTotalElements());
  }

  @Override
  public List<KeyValueResponse> getList() {
    Comparator<JobEntity> comparator =
        Comparator.comparing(
                JobEntity::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder()))
            .reversed();

    return jobRepository
        .findAllByEstadoAndGraduate_User_Estado(
            ConstantsUtils.STATUS_ACTIVE, ConstantsUtils.STATUS_ACTIVE)
        .stream()
        .sorted(comparator)
        .map(jobMapper::toKeyValueResponse)
        .toList();
  }

  @Override
  public List<KeyValueResponse> getListByGraduate(Long graduateId) {
    return jobRepository
        .findAllByEstadoAndGraduateId(ConstantsUtils.STATUS_ACTIVE, graduateId)
        .stream()
        .map(jobMapper::toKeyValueResponse)
        .toList();
  }
}
