package pe.com.graduate.insights.api.features.surveytype.application.ports.input;

import java.util.List;
import pe.com.graduate.insights.api.features.surveytype.application.dto.SurveyTypeRequest;
import pe.com.graduate.insights.api.features.surveytype.application.dto.SurveyTypeResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.shared.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.shared.ports.generic.GenericList;
import pe.com.graduate.insights.api.shared.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericRead;
import pe.com.graduate.insights.api.shared.ports.generic.GenericUpdate;

public interface SurveyTypeUseCase
    extends GenericCreate<SurveyTypeRequest>,
        GenericUpdate<SurveyTypeRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<SurveyTypeResponse>,
        GenericPaginate<SurveyTypeResponse>,
        GenericDelete {

  List<SurveyTypeResponse> getActiveTypes();
}
