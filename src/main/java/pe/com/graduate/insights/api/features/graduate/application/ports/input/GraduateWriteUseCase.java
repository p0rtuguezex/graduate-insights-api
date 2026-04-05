package pe.com.graduate.insights.api.features.graduate.application.ports.input;

import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;

public interface GraduateWriteUseCase {

  Long save(GraduateRequest request);

  void update(GraduateRequest request, Long id);

  void delete(Long id);

  void activate(Long id);
}
