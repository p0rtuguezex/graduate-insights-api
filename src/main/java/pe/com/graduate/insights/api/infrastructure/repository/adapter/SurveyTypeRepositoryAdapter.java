package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pe.com.graduate.insights.api.application.ports.output.SurveyTypeRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.SurveyTypeRequest;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyTypeResponse;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyTypeEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.SurveyTypeRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.SurveyTypeMapper;

@Repository
@RequiredArgsConstructor
public class SurveyTypeRepositoryAdapter implements SurveyTypeRepositoryPort {

  private final SurveyTypeRepository surveyTypeRepository;
  private final SurveyTypeMapper surveyTypeMapper;

  @Override
  public void save(SurveyTypeRequest request) {
    SurveyTypeEntity entity = surveyTypeMapper.toEntity(request);
    surveyTypeRepository.save(entity);
  }

  @Override
  public SurveyTypeResponse getDomain(Long id) {
    SurveyTypeEntity entity =
        surveyTypeRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException("Tipo de encuesta no encontrado con ID: " + id));
    return surveyTypeMapper.toDomain(entity);
  }

  @Override
  public void update(SurveyTypeRequest request, Long id) {
    SurveyTypeEntity entity =
        surveyTypeRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException("Tipo de encuesta no encontrado con ID: " + id));
    surveyTypeMapper.updateEntity(entity, request);
    surveyTypeRepository.save(entity);
  }

  @Override
  public void delete(Long id) {
    SurveyTypeEntity entity =
        surveyTypeRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException("Tipo de encuesta no encontrado con ID: " + id));
    surveyTypeRepository.delete(entity);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return surveyTypeRepository.findAll().stream()
        .map(surveyTypeMapper::toKeyValueResponse)
        .toList();
  }

  @Override
  public Page<SurveyTypeResponse> getPagination(String search, Pageable pageable) {
    Page<SurveyTypeEntity> entities = surveyTypeRepository.findBySearchTerm(search, pageable);
    return entities.map(surveyTypeMapper::toDomain);
  }

  @Override
  public List<SurveyTypeResponse> getActiveTypes() {
    return surveyTypeRepository.findByActiveTrue().stream()
        .map(surveyTypeMapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsByName(String name) {
    return surveyTypeRepository.existsByNameIgnoreCase(name);
  }

  @Override
  public boolean existsByNameAndIdNot(String name, Long id) {
    return surveyTypeRepository.existsByNameIgnoreCaseAndIdNot(name, id);
  }
}
