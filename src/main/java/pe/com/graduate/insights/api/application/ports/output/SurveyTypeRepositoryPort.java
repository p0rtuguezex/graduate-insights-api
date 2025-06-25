package pe.com.graduate.insights.api.application.ports.output;

import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.SurveyTypeRequest;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyTypeResponse;

import java.util.List;

public interface SurveyTypeRepositoryPort
    extends GenericCreate<SurveyTypeRequest>,
        GenericUpdate<SurveyTypeRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<SurveyTypeResponse>,
        GenericPaginate<SurveyTypeResponse>,
        GenericDelete {
    
    List<SurveyTypeResponse> getActiveTypes();
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
} 