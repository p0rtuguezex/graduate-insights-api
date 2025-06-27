package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.SurveyRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.exception.SurveyException;
import pe.com.graduate.insights.api.domain.models.request.SurveyRequest;
import pe.com.graduate.insights.api.domain.models.response.SurveyResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyTypeEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.SurveyRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.SurveyTypeRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.SurveyMapper;

import java.util.List;

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
                throw new SurveyException(String.format(ConstantsUtils.SURVEY_CONFLICT, request.getTitle()));
            },
            () -> {
                SurveyEntity surveyEntity = surveyMapper.toEntity(request);
                
                // Establecer la relación con SurveyType
                SurveyTypeEntity surveyType = surveyTypeRepository.findById(request.getSurveyTypeId())
                    .orElseThrow(() -> new NotFoundException("Tipo de encuesta no encontrado con ID: " + request.getSurveyTypeId()));
                surveyEntity.setSurveyType(surveyType);
                
                surveyRepository.save(surveyEntity);
            });
    }

    @Override
    public Page<SurveyResponse> getPagination(String search, Pageable pageable) {
        boolean hasSearch = !StringUtils.isEmpty(search);
        Page<SurveyEntity> surveyPage = hasSearch ?
                surveyRepository.findByTitleContainingIgnoreCase(search, pageable) :
                surveyRepository.findAll(pageable);
        
        List<SurveyResponse> surveyResponses = surveyPage.getContent().stream()
                .map(surveyMapper::toDomain)
                .toList();
                
        return new PageImpl<>(surveyResponses, pageable, surveyPage.getTotalElements());
    }

    @Override
    public SurveyResponse getDomain(Long id) {
        return surveyRepository.findById(id)
                .map(surveyMapper::toDomain)
                .orElseThrow(() -> new NotFoundException(
                    String.format(ConstantsUtils.SURVEY_NOT_FOUND, id)));
    }

    @Override
    public void update(SurveyRequest request, Long id) {
        surveyRepository
                .findById(id)
                .map(surveyEntity -> {
                    surveyMapper.updateSurveyEntity(request, surveyEntity);
                    
                    // Actualizar la relación con SurveyType si cambió
                    if (request.getSurveyTypeId() != null && 
                        !request.getSurveyTypeId().equals(surveyEntity.getSurveyType().getId())) {
                        SurveyTypeEntity surveyType = surveyTypeRepository.findById(request.getSurveyTypeId())
                            .orElseThrow(() -> new NotFoundException("Tipo de encuesta no encontrado con ID: " + request.getSurveyTypeId()));
                        surveyEntity.setSurveyType(surveyType);
                    }
                    
                    return surveyRepository.save(surveyEntity);
                })
                .orElseThrow(() -> new NotFoundException(String.format(ConstantsUtils.SURVEY_NOT_FOUND, id)));
    }

    @Override
    public void delete(Long id) {
        if (!surveyRepository.existsById(id)) {
            throw new NotFoundException(String.format(ConstantsUtils.SURVEY_NOT_FOUND, id));
        }
        surveyRepository.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyStatus status) {
        surveyRepository
                .findById(id)
                .map(surveyEntity -> {
                    surveyEntity.setStatus(status);
                    return surveyRepository.save(surveyEntity);
                })
                .orElseThrow(() -> new NotFoundException(String.format(ConstantsUtils.SURVEY_NOT_FOUND, id)));
    }

    @Override
    public List<SurveyResponse> getActiveSurveys() {
        List<SurveyEntity> activeSurveys = surveyRepository.findByStatus(
            pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyStatus.ACTIVE
        );
        return activeSurveys.stream()
                .map(surveyMapper::toDomain)
                .toList();
    }

    @Override
    public List<SurveyResponse> getSurveysByStatus(pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyStatus status) {
        List<SurveyEntity> surveys = surveyRepository.findByStatus(status);
        return surveys.stream()
                .map(surveyMapper::toDomain)
                .toList();
    }
} 