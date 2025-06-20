package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.domain.models.request.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateSurveyResponseEntity;

import java.util.List;

public interface GraduateSurveyResponseUseCase extends GenericCreate<GraduateSurveyResponseRequest> {
    List<GraduateSurveyResponseEntity> findBySurveyId(Long surveyId);
    List<GraduateSurveyResponseEntity> findByGraduateId(Long graduateId);
} 