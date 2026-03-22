package pe.com.graduate.insights.api.features.graduate.application.ports.output;

import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;

public interface GraduateWriteRepositoryPort {

  void save(GraduateRequest request);

  void update(GraduateRequest request, Long id);

  void delete(Long id);

  void updateValidationStatus(Long graduateId, Boolean validated);

  GraduateResponse getDomain(Long id);
}