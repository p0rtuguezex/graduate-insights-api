package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.domain.models.request.GraduateSelfRegistrationRequest;

public interface GraduateSelfRegistrationUseCase {
  void register(GraduateSelfRegistrationRequest request);
}
