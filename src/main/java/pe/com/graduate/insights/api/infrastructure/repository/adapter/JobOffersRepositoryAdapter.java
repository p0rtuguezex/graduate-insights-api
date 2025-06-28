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
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
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
  private final ValidationUtilsAdapter validationUtilsAdapter;

  @Override
  public List<JobOffersResponse> getJobsList(Long employerId) {
    var employer = validationUtilsAdapter.getAuthenticatedEmployerId(employerId);
    return Optional.of(
            jobOffersRepository.findAllByEstadoAndEmployerId(
                ConstantsUtils.STATUS_ACTIVE, employer.getId()))
        .filter(joOffersbs -> !joOffersbs.isEmpty())
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(
                        ConstantsUtils.JOB_OFFERS_NOT_FOUND_BY_EMPLOYER, employer.getId())))
        .stream()
        .map(jobOffersMapper::toJobOffersResponse)
        .collect(Collectors.toList());
  }

  @Override
  public JobOffersResponse getDomain(Long employerId, Long jobOfferId) {
    var employer = validationUtilsAdapter.getAuthenticatedEmployerId(employerId);
    return jobOffersRepository
        .findByIdAndEstadoAndEmployerId(jobOfferId, ConstantsUtils.STATUS_ACTIVE, employer.getId())
        .map(jobOffersMapper::toJobOffersResponse)
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, jobOfferId)));
  }

  @Override
  public void save(JobOffersRequest jobOffersRequest) {
    var employer =
        validationUtilsAdapter.getAuthenticatedEmployerId(jobOffersRequest.getEmployerId());
    JobOffersEntity jobOffersEntity = jobOffersMapper.toEntity(jobOffersRequest, employer);
    jobOffersRepository.save(jobOffersEntity);
  }

  @Override
  public void update(JobOffersRequest jobOffersRequest, Long jobOfferId) {
    var employer =
        validationUtilsAdapter.getAuthenticatedEmployerId(jobOffersRequest.getEmployerId());
    JobOffersEntity jobOffersEntity =
        jobOffersRepository
            .findByIdAndEstadoAndEmployerId(
                jobOfferId, ConstantsUtils.STATUS_ACTIVE, employer.getId())
            .orElseThrow(
                () ->
                    new NotFoundException(
                        String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, jobOfferId)));

    if (!jobOffersEntity.getEmployer().getId().equals(jobOffersRequest.getEmployerId())) {
      employerRepository
          .findByIdAndUserEstado(jobOffersRequest.getEmployerId(), ConstantsUtils.STATUS_ACTIVE)
          .ifPresentOrElse(
              newEmployer -> {
                jobOffersEntity.setEmployer(newEmployer);
                jobOffersMapper.updateJobOffersEntity(jobOffersRequest, jobOffersEntity);
                jobOffersRepository.save(jobOffersEntity);
              },
              () -> {
                throw new NotFoundException(
                    String.format(
                        ConstantsUtils.EMPLOYER_NOT_FOUND, jobOffersRequest.getEmployerId()));
              });
    } else {
      jobOffersMapper.updateJobOffersEntity(jobOffersRequest, jobOffersEntity);
      jobOffersRepository.save(jobOffersEntity);
    }
  }

  @Override
  public void delete(Long employerId, Long jobOfferId) {
    var employer = validationUtilsAdapter.getAuthenticatedEmployerId(employerId);
    jobOffersRepository
        .findByIdAndEstadoAndEmployerId(jobOfferId, ConstantsUtils.STATUS_ACTIVE, employer.getId())
        .ifPresentOrElse(
            jobEntity -> jobOffersRepository.deactivateJobOffers(jobOfferId),
            () -> {
              throw new NotFoundException(
                  String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, jobOfferId));
            });
  }

  @Override
  public Page<JobOffersResponse> getPagination(String search, Pageable pageable, Long employerId) {
    var employer = validationUtilsAdapter.getAuthenticatedEmployerId(employerId);
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<JobOffersEntity> jobOffersEntities =
        hasSearch
            ? jobOffersRepository.findAllByEstadoSearchAndEmployerId(
                search, ConstantsUtils.STATUS_ACTIVE, pageable, employer.getId())
            : jobOffersRepository.findAllByEstadoAndEmployerId(
                ConstantsUtils.STATUS_ACTIVE, pageable, employer.getId());
    List<JobOffersResponse> graduateResponseList =
        jobOffersEntities.getContent().stream().map(jobOffersMapper::toJobOffersResponse).toList();
    return new PageImpl<>(graduateResponseList, pageable, jobOffersEntities.getTotalElements());
  }

  @Override
  public List<KeyValueResponse> getJobOffersList() {
    return jobOffersRepository
        .findAllByEstadoAndEmployer_User_Estado(
            ConstantsUtils.STATUS_ACTIVE, ConstantsUtils.STATUS_ACTIVE)
        .stream()
        .map(jobOffersMapper::toKeyValueResponse)
        .toList();
  }
}
