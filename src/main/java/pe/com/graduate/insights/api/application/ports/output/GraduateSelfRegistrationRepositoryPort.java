package pe.com.graduate.insights.api.application.ports.output;

import pe.com.graduate.insights.api.domain.models.request.GraduateSelfRegistrationRequest;

public interface GraduateSelfRegistrationRepositoryPort {
  void register(GraduateSelfRegistrationRequest request);
}
