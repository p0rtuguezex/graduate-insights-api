package pe.com.graduate.insights.api.features.graduateselfregistration.application.ports.output;

import pe.com.graduate.insights.api.features.graduateselfregistration.application.dto.GraduateSelfRegistrationRequest;

public interface GraduateSelfRegistrationRepositoryPort {
  void register(GraduateSelfRegistrationRequest request);
}
