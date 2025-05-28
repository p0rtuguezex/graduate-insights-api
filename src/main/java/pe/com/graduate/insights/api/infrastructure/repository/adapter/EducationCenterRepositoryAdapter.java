package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.EducationCenterRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.EducationCenterException;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.EducationCenterRequest;
import pe.com.graduate.insights.api.domain.models.response.EducationCenterResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EducationCenterEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EducationCenterRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.EducationCenterMapper;

@Component
@RequiredArgsConstructor
public class EducationCenterRepositoryAdapter implements EducationCenterRepositoryPort {

  private final EducationCenterRepository educationCenterRepository;
  private final EducationCenterMapper educationCenterMapper;

  @Override
  public void save(EducationCenterRequest request) {
    educationCenterRepository
        .findByNombreAndEstado(request.getNombre(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            educationCenterEntity -> {
              throw new EducationCenterException(
                  String.format(ConstantsUtils.EDUCATION_CENTER_CONFLICT, request.getNombre()));
            },
            () -> {
              EducationCenterEntity educationCentEntity = educationCenterMapper.toEntity(request);
              educationCenterRepository.save(educationCentEntity);
            });
  }

  @Override
  public void delete(Long id) {
    educationCenterRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            educationCenterEntity -> educationCenterRepository.deactivateEducationCenter(id),
            () -> {
              throw new NotFoundException(
                  String.format(ConstantsUtils.EDUCATION_CENTER_NOT_FOUND_ID, id));
            });
  }

  @Override
  public List<EducationCenterResponse> getList() {
    return educationCenterRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .map(educationCenterMapper::toDomain)
        .toList();
  }

  @Override
  public Page<EducationCenterResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<EducationCenterEntity> educationCenterEntities =
        hasSearch
            ? educationCenterRepository.findAllByEstadoSearch(
                search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : educationCenterRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);
    List<EducationCenterResponse> educationCenterResponseList =
        educationCenterEntities.getContent().stream().map(educationCenterMapper::toDomain).toList();
    return new PageImpl<>(
        educationCenterResponseList, pageable, educationCenterEntities.getTotalElements());
  }

  @Override
  public EducationCenterResponse getDomain(Long id) {
    return educationCenterRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(educationCenterMapper::toDomain)
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(ConstantsUtils.EDUCATION_CENTER_NOT_FOUND_ID, id)));
  }

  @Override
  public void update(EducationCenterRequest request, Long id) {
    educationCenterRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(
            educationCenterEntity -> {
              educationCenterMapper.updateEducationCenterEntity(request, educationCenterEntity);
              return educationCenterRepository.save(educationCenterEntity);
            })
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(ConstantsUtils.EDUCATION_CENTER_NOT_FOUND_ID, id)));
  }
}
