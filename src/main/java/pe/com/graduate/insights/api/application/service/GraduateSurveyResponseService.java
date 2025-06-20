package pe.com.graduate.insights.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.GraduateSurveyResponseUseCase;
import pe.com.graduate.insights.api.application.ports.output.GraduateSurveyResponseRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateSurveyResponseEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraduateSurveyResponseService implements GraduateSurveyResponseUseCase {

    private final GraduateSurveyResponseRepositoryPort graduateSurveyResponseRepositoryPort;

    @Override
    public void save(GraduateSurveyResponseRequest request) {
        graduateSurveyResponseRepositoryPort.save(request);
    }

    @Override
    public List<GraduateSurveyResponseEntity> findBySurveyId(Long surveyId) {
        return graduateSurveyResponseRepositoryPort.findBySurveyId(surveyId);
    }

    @Override
    public List<GraduateSurveyResponseEntity> findByGraduateId(Long graduateId) {
        return graduateSurveyResponseRepositoryPort.findByGraduateId(graduateId);
    }
} 