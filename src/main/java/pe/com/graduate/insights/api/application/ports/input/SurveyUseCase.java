package pe.com.graduate.insights.api.application.ports.input;

import java.util.List;
import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.SurveyRequest;
import pe.com.graduate.insights.api.domain.models.response.SurveyResponse;

public interface SurveyUseCase
    extends GenericCreate<SurveyRequest>,
        GenericUpdate<SurveyRequest>,
        GenericRead<SurveyResponse>,
        GenericPaginate<SurveyResponse>,
        GenericDelete {

  /** Actualiza el estado de una encuesta */
  void updateStatus(Long id, String status);

  /** Obtiene todas las encuestas activas */
  List<SurveyResponse> getActiveSurveys();

  /** Obtiene encuestas por estado específico */
  List<SurveyResponse> getSurveysByStatus(String status);
}
