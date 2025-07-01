package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
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

  @Override
  public JobOffersResponse getDomain(Long id) {
    return jobOffersRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(jobOffersMapper::toJobOffersResponse)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, id)));
  }

  @Override
  public void save(JobOffersRequest jobOffersRequest) {
    var employer =
        employerRepository
            .findByIdAndUserEstado(jobOffersRequest.getEmployerId(), ConstantsUtils.STATUS_ACTIVE)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        String.format(
                            ConstantsUtils.EMPLOYER_NOT_FOUND, jobOffersRequest.getEmployerId())));
    JobOffersEntity jobOffersEntity = jobOffersMapper.toEntity(jobOffersRequest, employer);
    jobOffersRepository.save(jobOffersEntity);
  }

  @Override
  public void update(JobOffersRequest jobOffersRequest, Long id) {
    JobOffersEntity jobOffersEntity =
        jobOffersRepository
            .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
            .orElseThrow(
                () ->
                    new NotFoundException(String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, id)));

    // Verificar si se está cambiando el empleador
    if (!jobOffersEntity.getEmployer().getId().equals(jobOffersRequest.getEmployerId())) {
      var newEmployer =
          employerRepository
              .findByIdAndUserEstado(jobOffersRequest.getEmployerId(), ConstantsUtils.STATUS_ACTIVE)
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          String.format(
                              ConstantsUtils.EMPLOYER_NOT_FOUND,
                              jobOffersRequest.getEmployerId())));
      jobOffersEntity.setEmployer(newEmployer);
    }

    jobOffersMapper.updateJobOffersEntity(jobOffersRequest, jobOffersEntity);
    jobOffersRepository.save(jobOffersEntity);
  }

  @Override
  public void delete(Long id) {
    jobOffersRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            jobOffersEntity -> jobOffersRepository.deactivateJobOffers(id),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, id));
            });
  }

  @Override
  public Page<JobOffersResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<JobOffersEntity> jobOffersEntities =
        hasSearch
            ? jobOffersRepository.findAllByEstadoAndSearch(
                search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : jobOffersRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);

    List<JobOffersResponse> jobOffersResponseList =
        jobOffersEntities.getContent().stream().map(jobOffersMapper::toJobOffersResponse).toList();
    return new PageImpl<>(jobOffersResponseList, pageable, jobOffersEntities.getTotalElements());
  }

  @Override
  public List<KeyValueResponse> getList() {
    return jobOffersRepository
        .findAllByEstadoAndEmployer_User_Estado(
            ConstantsUtils.STATUS_ACTIVE, ConstantsUtils.STATUS_ACTIVE)
        .stream()
        .map(jobOffersMapper::toKeyValueResponse)
        .toList();
  }

  // Implementación de métodos con lógica de roles
  @Override
  public JobOffersResponse getDomainByRole(Long id, boolean isDirector, Long currentUserId) {
    if (isDirector) {
      // Director ve cualquier oferta de trabajo con todos los atributos
      return jobOffersRepository
          .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
          .map(jobOffersMapper::toJobOffersResponse)
          .orElseThrow(
              () -> new NotFoundException(String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, id)));
    } else {
      // Employer solo ve sus propias ofertas de trabajo y sin employer_id
      Long employerId = getEmployerIdByUserId(currentUserId);
      return jobOffersRepository
          .findByIdAndEstadoAndEmployerId(id, ConstantsUtils.STATUS_ACTIVE, employerId)
          .map(
              jobOffersMapper
                  ::toJobOffersResponseWithoutEmployerId) // Método especial sin employer_id
          .orElseThrow(
              () -> new NotFoundException(String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, id)));
    }
  }

  @Override
  public void saveByRole(
      JobOffersRequest jobOffersRequest, boolean isDirector, Long currentUserId) {
    if (isDirector) {
      // Director puede crear oferta de trabajo para cualquier empleador
      var employer =
          employerRepository
              .findByIdAndUserEstado(jobOffersRequest.getEmployerId(), ConstantsUtils.STATUS_ACTIVE)
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          String.format(
                              ConstantsUtils.EMPLOYER_NOT_FOUND,
                              jobOffersRequest.getEmployerId())));
      JobOffersEntity jobOffersEntity = jobOffersMapper.toEntity(jobOffersRequest, employer);
      jobOffersRepository.save(jobOffersEntity);
    } else {
      // Employer solo puede crear ofertas de trabajo para sí mismo
      Long employerId = getEmployerIdByUserId(currentUserId);
      var employer =
          employerRepository
              .findByIdAndUserEstado(employerId, ConstantsUtils.STATUS_ACTIVE)
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          String.format(ConstantsUtils.EMPLOYER_NOT_FOUND, employerId)));

      // Forzar que el employer_id sea el del usuario autenticado
      jobOffersRequest.setEmployerId(employerId);
      JobOffersEntity jobOffersEntity = jobOffersMapper.toEntity(jobOffersRequest, employer);
      jobOffersRepository.save(jobOffersEntity);
    }
  }

  @Override
  public void updateByRole(
      JobOffersRequest jobOffersRequest, Long id, boolean isDirector, Long currentUserId) {
    if (isDirector) {
      // Director puede actualizar cualquier oferta de trabajo
      JobOffersEntity jobOffersEntity =
          jobOffersRepository
              .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, id)));

      // Verificar si se está cambiando el empleador
      if (!jobOffersEntity.getEmployer().getId().equals(jobOffersRequest.getEmployerId())) {
        var newEmployer =
            employerRepository
                .findByIdAndUserEstado(
                    jobOffersRequest.getEmployerId(), ConstantsUtils.STATUS_ACTIVE)
                .orElseThrow(
                    () ->
                        new NotFoundException(
                            String.format(
                                ConstantsUtils.EMPLOYER_NOT_FOUND,
                                jobOffersRequest.getEmployerId())));
        jobOffersEntity.setEmployer(newEmployer);
      }

      jobOffersMapper.updateJobOffersEntity(jobOffersRequest, jobOffersEntity);
      jobOffersRepository.save(jobOffersEntity);
    } else {
      // Employer solo puede actualizar sus propias ofertas de trabajo
      Long employerId = getEmployerIdByUserId(currentUserId);
      JobOffersEntity jobOffersEntity =
          jobOffersRepository
              .findByIdAndEstadoAndEmployerId(id, ConstantsUtils.STATUS_ACTIVE, employerId)
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, id)));

      // Asegurar que el employer_id no cambie
      jobOffersRequest.setEmployerId(employerId);
      jobOffersMapper.updateJobOffersEntity(jobOffersRequest, jobOffersEntity);
      jobOffersRepository.save(jobOffersEntity);
    }
  }

  @Override
  public void deleteByRole(Long id, boolean isDirector, Long currentUserId) {
    if (isDirector) {
      // Director puede eliminar cualquier oferta de trabajo
      jobOffersRepository
          .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
          .ifPresentOrElse(
              jobOffersEntity -> jobOffersRepository.deactivateJobOffers(id),
              () -> {
                throw new NotFoundException(String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, id));
              });
    } else {
      // Employer solo puede eliminar sus propias ofertas de trabajo
      Long employerId = getEmployerIdByUserId(currentUserId);
      jobOffersRepository
          .findByIdAndEstadoAndEmployerId(id, ConstantsUtils.STATUS_ACTIVE, employerId)
          .ifPresentOrElse(
              jobOffersEntity -> jobOffersRepository.deactivateJobOffers(id),
              () -> {
                throw new NotFoundException(String.format(ConstantsUtils.JOB_OFFERS_NOT_FOUND, id));
              });
    }
  }

  @Override
  public Page<JobOffersResponse> getPaginationByRole(
      String search, Pageable pageable, boolean isDirector, Long currentUserId) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    if (isDirector) {
      // Director ve todas las ofertas de trabajo con todos los atributos
      Page<JobOffersEntity> jobOffersEntities =
          hasSearch
              ? jobOffersRepository.findAllByEstadoAndSearch(
                  search, ConstantsUtils.STATUS_ACTIVE, pageable)
              : jobOffersRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);

      List<JobOffersResponse> jobOffersResponseList =
          jobOffersEntities.getContent().stream()
              .map(jobOffersMapper::toJobOffersResponse)
              .toList();
      return new PageImpl<>(jobOffersResponseList, pageable, jobOffersEntities.getTotalElements());
    } else {
      // Employer ve solo sus ofertas de trabajo sin employer_id
      Long employerId = getEmployerIdByUserId(currentUserId);
      Page<JobOffersEntity> jobOffersEntities =
          hasSearch
              ? jobOffersRepository.findAllByEstadoSearchAndEmployerId(
                  search, ConstantsUtils.STATUS_ACTIVE, pageable, employerId)
              : jobOffersRepository.findAllByEstadoAndEmployerId(
                  ConstantsUtils.STATUS_ACTIVE, pageable, employerId);

      List<JobOffersResponse> jobOffersResponseList =
          jobOffersEntities.getContent().stream()
              .map(jobOffersMapper::toJobOffersResponseWithoutEmployerId)
              .toList();
      return new PageImpl<>(jobOffersResponseList, pageable, jobOffersEntities.getTotalElements());
    }
  }

  /** Método auxiliar para obtener el employerId asociado al userId */
  private Long getEmployerIdByUserId(Long userId) {
    return employerRepository
        .findByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE)
        .map(employer -> employer.getId())
        .orElseThrow(
            () ->
                new NotFoundException(
                    "El usuario autenticado no es un empleador o no está activo"));
  }
}
