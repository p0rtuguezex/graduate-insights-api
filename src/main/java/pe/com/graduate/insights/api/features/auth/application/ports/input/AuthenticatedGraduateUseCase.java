package pe.com.graduate.insights.api.features.auth.application.ports.input;

import org.springframework.security.core.Authentication;

public interface AuthenticatedGraduateUseCase {
  Long getAuthenticatedGraduateId(Authentication authentication);
}
