package pe.com.graduate.insights.api.application.ports.output;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

public interface GraduateRepositoryPort
    extends GenericCreate<GraduateRequest>,
        GenericUpdate<GraduateRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<GraduateResponse>,
        GenericPaginate<GraduateResponse>,
        GenericDelete {

  /**
   * Actualiza la ruta del CV de un graduado
   *
   * @param graduateId ID del graduado
   * @param cvPath Nueva ruta del CV (puede ser null para eliminar)
   */
  void updateCvPath(Long graduateId, String cvPath);

  Page<GraduateResponse> getPagination(String search, Pageable pageable, Boolean validated);

  void updateValidationStatus(Long graduateId, Boolean validated);

  Long getActiveGraduateIdByUserId(Long userId);
}
