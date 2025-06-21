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
import pe.com.graduate.insights.api.infrastructure.repository.jpa.SurveyRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.SurveyMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SurveyRepositoryAdapter implements SurveyRepositoryPort {

    private final SurveyRepository surveyRepository;
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
} 