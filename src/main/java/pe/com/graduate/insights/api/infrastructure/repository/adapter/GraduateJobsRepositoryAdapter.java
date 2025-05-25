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
import pe.com.graduate.insights.api.application.ports.output.GraduateJobsRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.GraduateJobRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateJobResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.JobEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateJobRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.GraduateJobMapper;

@Component
@RequiredArgsConstructor
public class GraduateJobsRepositoryAdapter implements GraduateJobsRepositoryPort {

  private final GraduateJobRepository graduateJobRepository;
  private final GraduateRepository graduateRepository;
  private final GraduateJobMapper graduateJobMapper;

  @Override
  public List<GraduateJobResponse> getJobsList(Long graduateId) {
    if (!graduateRepository.existsByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE)) {
      throw new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, graduateId));
    }
    return Optional.of(
            graduateJobRepository.findAllByEstadoAndGraduateId(
                ConstantsUtils.STATUS_ACTIVE, graduateId))
        .filter(jobs -> !jobs.isEmpty())
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(ConstantsUtils.JOB_NOT_FOUND_BY_GRADUATE, graduateId)))
        .stream()
        .map(graduateJobMapper::toJobResponse)
        .collect(Collectors.toList());
  }

  @Override
  public GraduateJobResponse getDomain(Long graduateId, Long jobId) {
    if (!graduateRepository.existsByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE)) {
      throw new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, graduateId));
    }

    return graduateJobRepository
        .findByIdAndEstadoAndGraduateId(jobId, ConstantsUtils.STATUS_ACTIVE, graduateId)
        .map(graduateJobMapper::toJobResponse)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, jobId)));
  }

  @Override
  public void save(GraduateJobRequest graduateJobRequest, Long graduateId) {
    graduateRepository
        .findByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            graduateEntity -> {
              JobEntity jobEntity = graduateJobMapper.toEntity(graduateJobRequest, graduateEntity);
              graduateJobRepository.save(jobEntity);
            },
            () -> {
              throw new NotFoundException(
                  String.format(ConstantsUtils.GRADUATE_NOT_FOUND, graduateId));
            });
  }

  @Override
  public void update(GraduateJobRequest graduateJobRequest, Long graduateId, Long jobId) {
    //    Optional<GraduateEntity> graduateEntity =
    //        graduateRepository.findByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE);
    //    if (graduateEntity.isEmpty()) {
    //      throw new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND,
    // graduateId));
    //    }
    if (!graduateRepository.existsByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE)) {
      throw new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, graduateId));
    }

    graduateJobRepository
        .findByIdAndEstadoAndGraduateId(jobId, ConstantsUtils.STATUS_ACTIVE, graduateId)
        .map(
            jobEntity -> {
              graduateJobMapper.updateJobEntity(graduateJobRequest, jobEntity);
              return graduateJobRepository.save(jobEntity);
            })
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, jobId)));
  }

  @Override
  public void delete(Long graduateId, Long jobId) {
    //    Optional<GraduateEntity> graduateEntity =
    //        graduateRepository.findByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE);
    //    if (graduateEntity.isEmpty()) {
    //      throw new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND,
    // graduateId));
    //    }
    if (!graduateRepository.existsByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE)) {
      throw new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, graduateId));
    }

    graduateJobRepository
        .findByIdAndEstadoAndGraduateId(jobId, ConstantsUtils.STATUS_ACTIVE, graduateId)
        .ifPresentOrElse(
            jobEntity -> graduateJobRepository.deactivateJob(jobId),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, jobId));
            });
  }

  @Override
  public Page<GraduateJobResponse> getPagination(
      String search, Pageable pageable, Long graduateId) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<JobEntity> jobEntities =
        hasSearch
            ? graduateJobRepository.findAllByEstadoSearch(
                search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : graduateJobRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);
    List<GraduateJobResponse> graduateResponseList =
        jobEntities.getContent().stream().map(graduateJobMapper::toJobResponse).toList();
    return new PageImpl<>(graduateResponseList, pageable, jobEntities.getTotalElements());
  }
}
