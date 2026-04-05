package pe.com.graduate.insights.api.features.survey.application.ports.output;

import java.util.List;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyRequest;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;
import pe.com.graduate.insights.api.shared.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.shared.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericRead;
import pe.com.graduate.insights.api.shared.ports.generic.GenericUpdate;

public interface SurveyRepositoryPort
    extends GenericCreate<SurveyRequest>,
        GenericUpdate<SurveyRequest>,
        GenericRead<SurveyResponse>,
        GenericPaginate<SurveyResponse>,
        GenericDelete {

  void updateStatus(Long id, SurveyStatus status);

  List<SurveyResponse> getActiveSurveys();

  List<SurveyResponse> getSurveysByStatus(SurveyStatus status);
}
