package pe.com.graduate.insights.api.features.survey.application.ports.input;

import java.util.List;
import pe.com.graduate.insights.api.shared.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.shared.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericRead;
import pe.com.graduate.insights.api.shared.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyRequest;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;

public interface SurveyUseCase
    extends GenericCreate<SurveyRequest>,
        GenericUpdate<SurveyRequest>,
        GenericRead<SurveyResponse>,
        GenericPaginate<SurveyResponse>,
        GenericDelete {

  void updateStatus(Long id, String status);

  List<SurveyResponse> getActiveSurveys();

  List<SurveyResponse> getSurveysByStatus(String status);

  void notifyGraduates(Long surveyId);
}

