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
import pe.com.graduate.insights.api.application.ports.output.JobOffersRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.JobOffersRequest;
import pe.com.graduate.insights.api.domain.models.response.JobOffersResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.JobOffersEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EmployerRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.JobOffersRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.JobOffersMapper;

@Component
@RequiredArgsConstructor
public class JobOffersRepositoryAdapter implements JobOffersRepositoryPort {

  private final JobOffersRepository jobOffersRepository;
  private final EmployerRepository employerRepository;
  private final JobOffersMapper jobOffersMapper;

  @Override
  public List<JobOffersResponse> getJobsList(Long employerId) {
    if (!employerRepository.existsByIdAndUserEstado(employerId, ConstantsUtils.STATUS_ACTIVE)) {
      throw new NotFoundException(String.format(ConstantsUtils.EMPLOYER_NOT_FOUND, employerId));
    }
    return Optional.of(
            jobOffersRepository.findAllByEstadoAndEmployerId(
                ConstantsUtils.STATUS_ACTIVE, employerId))
        .filter(jobs -> !jobs.isEmpty())
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND_BY_EMPLOYER, employerId)))
        .stream()
        .map(jobOffersMapper::toJobOffersResponse)
        .collect(Collectors.toList());
  }

  @Override
  public JobOffersResponse getDomain(Long employerId, Long jobOfferId) {
    if (!employerRepository.existsByIdAndUserEstado(employerId, ConstantsUtils.STATUS_ACTIVE)) {
      throw new NotFoundException(String.format(ConstantsUtils.EMPLOYER_NOT_FOUND, employerId));
    }

    return jobOffersRepository
        .findByIdAndEstadoAndEmployerId(jobOfferId, ConstantsUtils.STATUS_ACTIVE, employerId)
        .map(jobOffersMapper::toJobOffersResponse)
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, jobOfferId)));
  }

  @Override
  public void save(JobOffersRequest jobOffersRequest, Long employerId) {
    employerRepository
        .findByIdAndUserEstado(employerId, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            employerEntity -> {
              JobOffersEntity jobOffersEntity =
                  jobOffersMapper.toEntity(jobOffersRequest, employerEntity);
              jobOffersRepository.save(jobOffersEntity);
            },
            () -> {
              throw new NotFoundException(
                  String.format(ConstantsUtils.EMPLOYER_NOT_FOUND, employerId));
            });
  }

  @Override
  public void update(JobOffersRequest jobOffersRequest, Long employerId, Long jobOfferId) {
    if (!employerRepository.existsByIdAndUserEstado(employerId, ConstantsUtils.STATUS_ACTIVE)) {
      throw new NotFoundException(String.format(ConstantsUtils.EMPLOYER_NOT_FOUND, employerId));
    }

    jobOffersRepository
        .findByIdAndEstadoAndEmployerId(jobOfferId, ConstantsUtils.STATUS_ACTIVE, employerId)
        .map(
            jobOffersEntity -> {
              jobOffersMapper.updateJobOffersEntity(jobOffersRequest, jobOffersEntity);
              return jobOffersRepository.save(jobOffersEntity);
            })
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, jobOfferId)));
  }

  @Override
  public void delete(Long employerId, Long jobOfferId) {
    if (!employerRepository.existsByIdAndUserEstado(employerId, ConstantsUtils.STATUS_ACTIVE)) {
      throw new NotFoundException(String.format(ConstantsUtils.EMPLOYER_NOT_FOUND, employerId));
    }

    jobOffersRepository
        .findByIdAndEstadoAndEmployerId(jobOfferId, ConstantsUtils.STATUS_ACTIVE, employerId)
        .ifPresentOrElse(
            jobEntity -> jobOffersRepository.deactivateJobOffers(jobOfferId),
            () -> {
              throw new NotFoundException(
                  String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, jobOfferId));
            });
  }

  @Override
  public Page<JobOffersResponse> getPagination(String search, Pageable pageable, Long employerId) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<JobOffersEntity> jobOffersEntities =
        hasSearch
            ? jobOffersRepository.findAllByEstadoSearchAndEmployerId(
                search, ConstantsUtils.STATUS_ACTIVE, pageable, employerId)
            : jobOffersRepository.findAllByEstadoAndEmployerId(
                ConstantsUtils.STATUS_ACTIVE, pageable, employerId);
    List<JobOffersResponse> graduateResponseList =
        jobOffersEntities.getContent().stream().map(jobOffersMapper::toJobOffersResponse).toList();
    return new PageImpl<>(graduateResponseList, pageable, jobOffersEntities.getTotalElements());
  }
}
