package pe.com.graduate.insights.api.features.survey.infrastructure.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.features.survey.domain.exception.SurveyException;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyRequest;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;
import pe.com.graduate.insights.api.features.survey.application.ports.output.SurveyRepositoryPort;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyEntity;
import pe.com.graduate.insights.api.features.surveytype.infrastructure.entity.SurveyTypeEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.jpa.SurveyRepository;
import pe.com.graduate.insights.api.features.surveytype.infrastructure.jpa.SurveyTypeRepository;
import pe.com.graduate.insights.api.features.survey.infrastructure.mapper.SurveyMapper;

@Component
@RequiredArgsConstructor
public class SurveyRepositoryAdapter implements SurveyRepositoryPort {

  private final SurveyRepository surveyRepository;
  private final SurveyTypeRepository surveyTypeRepository;
  private final SurveyMapper surveyMapper;

  @Override
  public void save(SurveyRequest request) {
    surveyRepository
        .findByTitle(request.getTitle())
        .ifPresentOrElse(
            survey -> {
              throw new SurveyException(
                  String.format(ConstantsUtils.SURVEY_CONFLICT, request.getTitle()));
            },
            () -> {
              SurveyEntity surveyEntity = surveyMapper.toEntity(request);

              // Establecer la relación con SurveyType
              SurveyTypeEntity surveyType =
                  surveyTypeRepository
                      .findById(request.getSurveyTypeId())
                      .orElseThrow(
                          () ->
                              new NotFoundException(
                                  "Tipo de encuesta no encontrado con ID: "
                                      + request.getSurveyTypeId()));
              surveyEntity.setSurveyType(surveyType);

              surveyRepository.save(surveyEntity);
            });
  }

  @Override
  public Page<SurveyResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);
    Page<SurveyEntity> surveyPage =
        hasSearch
            ? surveyRepository.findByTitleContainingIgnoreCase(search, pageable)
            : surveyRepository.findAll(pageable);

    List<SurveyResponse> surveyResponses =
        surveyPage.getContent().stream().map(surveyMapper::toDomain).toList();

    return new PageImpl<>(surveyResponses, pageable, surveyPage.getTotalElements());
  }

  @Override
  public SurveyResponse getDomain(Long id) {
    return surveyRepository
        .findById(id)
        .map(surveyMapper::toDomain)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.SURVEY_NOT_FOUND, id)));
  }

  @Override
  public void update(SurveyRequest request, Long id) {
    surveyRepository
        .findById(id)
        .map(
            surveyEntity -> {
              surveyMapper.updateSurveyEntity(request, surveyEntity);

              // Actualizar la relación con SurveyType si cambió
              if (request.getSurveyTypeId() != null
                  && !request.getSurveyTypeId().equals(surveyEntity.getSurveyType().getId())) {
                SurveyTypeEntity surveyType =
                    surveyTypeRepository
                        .findById(request.getSurveyTypeId())
                        .orElseThrow(
                            () ->
                                new NotFoundException(
                                    "Tipo de encuesta no encontrado con ID: "
                                        + request.getSurveyTypeId()));
                surveyEntity.setSurveyType(surveyType);
              }

              return surveyRepository.save(surveyEntity);
            })
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.SURVEY_NOT_FOUND, id)));
  }

  @Override
  public void delete(Long id) {
    if (!surveyRepository.existsById(id)) {
      throw new NotFoundException(String.format(ConstantsUtils.SURVEY_NOT_FOUND, id));
    }
    surveyRepository.deleteById(id);
  }

  @Override
  public void updateStatus(Long id, SurveyStatus status) {
    surveyRepository
        .findById(id)
        .map(
            surveyEntity -> {
              surveyEntity.setStatus(toEntityStatus(status));
              return surveyRepository.save(surveyEntity);
            })
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.SURVEY_NOT_FOUND, id)));
  }

  @Override
  public List<SurveyResponse> getActiveSurveys() {
    List<SurveyEntity> activeSurveys =
        surveyRepository.findByStatus(
            pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus.ACTIVE);
    return activeSurveys.stream().map(surveyMapper::toDomain).toList();
  }

  @Override
  public List<SurveyResponse> getSurveysByStatus(SurveyStatus status) {
    List<SurveyEntity> surveys = surveyRepository.findByStatus(toEntityStatus(status));
    return surveys.stream().map(surveyMapper::toDomain).toList();
  }

  private pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus
      toEntityStatus(SurveyStatus status) {
    return pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus
        .valueOf(status.name());
  }
}






