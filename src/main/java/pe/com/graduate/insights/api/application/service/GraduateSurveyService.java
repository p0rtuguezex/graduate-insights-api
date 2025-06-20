package pe.com.graduate.insights.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.GraduateSurveyUseCase;
import pe.com.graduate.insights.api.application.ports.output.GraduateSurveyRepositoryPort;
import pe.com.graduate.insights.api.domain.models.response.GraduateSurveyDetailResponse;
import pe.com.graduate.insights.api.domain.models.response.GraduateSurveyListResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraduateSurveyService implements GraduateSurveyUseCase {

    private final GraduateSurveyRepositoryPort graduateSurveyRepositoryPort;

    @Override
    public List<GraduateSurveyListResponse> getAllSurveysForGraduate(Long graduateId) {
        return graduateSurveyRepositoryPort.getAllSurveysForGraduate(graduateId);
    }

    @Override
    public List<GraduateSurveyListResponse> getCompletedSurveysForGraduate(Long graduateId) {
        return graduateSurveyRepositoryPort.getCompletedSurveysForGraduate(graduateId);
    }

    @Override
    public List<GraduateSurveyListResponse> getPendingSurveysForGraduate(Long graduateId) {
        return graduateSurveyRepositoryPort.getPendingSurveysForGraduate(graduateId);
    }

    @Override
    public GraduateSurveyDetailResponse getSurveyDetailForGraduate(Long surveyId, Long graduateId) {
        return graduateSurveyRepositoryPort.getSurveyDetailForGraduate(surveyId, graduateId);
    }
} 