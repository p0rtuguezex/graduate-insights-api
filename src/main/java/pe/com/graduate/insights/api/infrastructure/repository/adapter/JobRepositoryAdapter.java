package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
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
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
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

  @Override
  public JobResponse getDomain(Long id) {
    return jobRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(jobMapper::toJobResponse)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id)));
  }

  @Override
  public void save(JobRequest jobRequest) {
    var graduate = graduateRepository
        .findByIdAndUserEstado(jobRequest.getGraduateId(), ConstantsUtils.STATUS_ACTIVE)
        .orElseThrow(
            () -> new NotFoundException(
                String.format(ConstantsUtils.GRADUATE_NOT_FOUND, jobRequest.getGraduateId())));
    JobEntity jobEntity = jobMapper.toEntity(jobRequest, graduate);
    jobRepository.save(jobEntity);
  }

  @Override
  public void update(JobRequest jobRequest, Long id) {
    JobEntity jobEntity = jobRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id)));

    // Verificar si se está cambiando el graduado
    if (!jobEntity.getGraduate().getId().equals(jobRequest.getGraduateId())) {
      var newGraduate = graduateRepository
          .findByIdAndUserEstado(jobRequest.getGraduateId(), ConstantsUtils.STATUS_ACTIVE)
          .orElseThrow(
              () -> new NotFoundException(
                  String.format(ConstantsUtils.GRADUATE_NOT_FOUND, jobRequest.getGraduateId())));
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
  public Page<JobResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<JobEntity> jobEntities = hasSearch
        ? jobRepository.findAllByEstadoAndSearch(search, ConstantsUtils.STATUS_ACTIVE, pageable)
        : jobRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);

    List<JobResponse> jobResponseList =
        jobEntities.getContent().stream().map(jobMapper::toJobResponse).toList();
    return new PageImpl<>(jobResponseList, pageable, jobEntities.getTotalElements());
  }

  @Override
  public List<KeyValueResponse> getList() {
    return jobRepository
        .findAllByEstadoAndGraduate_User_Estado(
            ConstantsUtils.STATUS_ACTIVE, ConstantsUtils.STATUS_ACTIVE)
        .stream()
        .map(jobMapper::toKeyValueResponse)
        .toList();
  }

  // Implementación de métodos con lógica de roles
  @Override
  public JobResponse getDomainByRole(Long id, boolean isDirector, Long currentUserId) {
    if (isDirector) {
      // Director ve cualquier trabajo con todos los atributos
      return jobRepository
          .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
          .map(jobMapper::toJobResponse)
          .orElseThrow(
              () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id)));
    } else {
      // Graduado solo ve sus propios trabajos y sin graduate_id
      Long graduateId = getGraduateIdByUserId(currentUserId);
      return jobRepository
          .findByIdAndEstadoAndGraduateId(id, ConstantsUtils.STATUS_ACTIVE, graduateId)
          .map(jobMapper::toJobResponseWithoutGraduateId) // Método especial sin graduate_id
          .orElseThrow(
              () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id)));
    }
  }

  @Override
  public void saveByRole(JobRequest jobRequest, boolean isDirector, Long currentUserId) {
    if (isDirector) {
      // Director puede crear trabajo para cualquier graduado
      var graduate = graduateRepository
          .findByIdAndUserEstado(jobRequest.getGraduateId(), ConstantsUtils.STATUS_ACTIVE)
          .orElseThrow(
              () -> new NotFoundException(
                  String.format(ConstantsUtils.GRADUATE_NOT_FOUND, jobRequest.getGraduateId())));
      JobEntity jobEntity = jobMapper.toEntity(jobRequest, graduate);
      jobRepository.save(jobEntity);
    } else {
      // Graduado solo puede crear trabajos para sí mismo
      Long graduateId = getGraduateIdByUserId(currentUserId);
      var graduate = graduateRepository
          .findByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE)
          .orElseThrow(
              () -> new NotFoundException(
                  String.format(ConstantsUtils.GRADUATE_NOT_FOUND, graduateId)));
      
      // Forzar que el graduate_id sea el del usuario autenticado
      jobRequest.setGraduateId(graduateId);
      JobEntity jobEntity = jobMapper.toEntity(jobRequest, graduate);
      jobRepository.save(jobEntity);
    }
  }

  @Override
  public void updateByRole(JobRequest jobRequest, Long id, boolean isDirector, Long currentUserId) {
    if (isDirector) {
      // Director puede actualizar cualquier trabajo
      JobEntity jobEntity = jobRepository
          .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
          .orElseThrow(
              () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id)));

      // Verificar si se está cambiando el graduado
      if (!jobEntity.getGraduate().getId().equals(jobRequest.getGraduateId())) {
        var newGraduate = graduateRepository
            .findByIdAndUserEstado(jobRequest.getGraduateId(), ConstantsUtils.STATUS_ACTIVE)
            .orElseThrow(
                () -> new NotFoundException(
                    String.format(ConstantsUtils.GRADUATE_NOT_FOUND, jobRequest.getGraduateId())));
        jobEntity.setGraduate(newGraduate);
      }
      
      jobMapper.updateJobEntity(jobRequest, jobEntity);
      jobRepository.save(jobEntity);
    } else {
      // Graduado solo puede actualizar sus propios trabajos
      Long graduateId = getGraduateIdByUserId(currentUserId);
      JobEntity jobEntity = jobRepository
          .findByIdAndEstadoAndGraduateId(id, ConstantsUtils.STATUS_ACTIVE, graduateId)
          .orElseThrow(
              () -> new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id)));
      
      // Asegurar que el graduate_id no cambie
      jobRequest.setGraduateId(graduateId);
      jobMapper.updateJobEntity(jobRequest, jobEntity);
      jobRepository.save(jobEntity);
    }
  }

  @Override
  public void deleteByRole(Long id, boolean isDirector, Long currentUserId) {
    if (isDirector) {
      // Director puede eliminar cualquier trabajo
      jobRepository
          .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
          .ifPresentOrElse(
              jobEntity -> jobRepository.deactivateJob(id),
              () -> {
                throw new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id));
              });
    } else {
      // Graduado solo puede eliminar sus propios trabajos
      Long graduateId = getGraduateIdByUserId(currentUserId);
      jobRepository
          .findByIdAndEstadoAndGraduateId(id, ConstantsUtils.STATUS_ACTIVE, graduateId)
          .ifPresentOrElse(
              jobEntity -> jobRepository.deactivateJob(id),
              () -> {
                throw new NotFoundException(String.format(ConstantsUtils.JOB_NOT_FOUND, id));
              });
    }
  }

  @Override
  public Page<JobResponse> getPaginationByRole(String search, Pageable pageable, boolean isDirector, Long currentUserId) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    if (isDirector) {
      // Director ve todos los trabajos con todos los atributos
      Page<JobEntity> jobEntities = hasSearch
          ? jobRepository.findAllByEstadoAndSearch(search, ConstantsUtils.STATUS_ACTIVE, pageable)
          : jobRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);

      List<JobResponse> jobResponseList =
          jobEntities.getContent().stream().map(jobMapper::toJobResponse).toList();
      return new PageImpl<>(jobResponseList, pageable, jobEntities.getTotalElements());
    } else {
      // Graduado ve solo sus trabajos sin graduate_id
      Long graduateId = getGraduateIdByUserId(currentUserId);
      Page<JobEntity> jobEntities = hasSearch
          ? jobRepository.findAllByEstadoAndSearchAndGraduateId(
              search, ConstantsUtils.STATUS_ACTIVE, pageable, graduateId)
          : jobRepository.findAllByEstadoAndGraduateId(
              ConstantsUtils.STATUS_ACTIVE, pageable, graduateId);

      List<JobResponse> jobResponseList =
          jobEntities.getContent().stream().map(jobMapper::toJobResponseWithoutGraduateId).toList();
      return new PageImpl<>(jobResponseList, pageable, jobEntities.getTotalElements());
    }
  }

  @Override
  public List<KeyValueResponse> getListByRole(boolean isDirector, Long currentUserId) {
    if (isDirector) {
      // Director ve todos los trabajos
      return jobRepository
          .findAllByEstadoAndGraduate_User_Estado(
              ConstantsUtils.STATUS_ACTIVE, ConstantsUtils.STATUS_ACTIVE)
          .stream()
          .map(jobMapper::toKeyValueResponse)
          .toList();
    } else {
      // Graduado ve solo sus trabajos
      Long graduateId = getGraduateIdByUserId(currentUserId);
      return jobRepository
          .findAllByEstadoAndGraduateId(ConstantsUtils.STATUS_ACTIVE, graduateId)
          .stream()
          .map(jobMapper::toKeyValueResponse)
          .toList();
    }
  }

  /**
   * Método auxiliar para obtener el graduateId asociado al userId
   */
  private Long getGraduateIdByUserId(Long userId) {
    return graduateRepository
        .findByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE)
        .map(graduate -> graduate.getId())
        .orElseThrow(
            () -> new NotFoundException("El usuario autenticado no es un graduado o no está activo"));
  }
}
