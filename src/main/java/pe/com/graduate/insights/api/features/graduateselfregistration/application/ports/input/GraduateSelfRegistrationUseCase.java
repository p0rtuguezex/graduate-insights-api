package pe.com.graduate.insights.api.features.graduateselfregistration.application.ports.input;

import pe.com.graduate.insights.api.features.graduateselfregistration.application.dto.GraduateSelfRegistrationRequest;

public interface GraduateSelfRegistrationUseCase {
  void register(GraduateSelfRegistrationRequest request);
}
