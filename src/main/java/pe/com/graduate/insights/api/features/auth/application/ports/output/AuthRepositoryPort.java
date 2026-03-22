package pe.com.graduate.insights.api.features.auth.application.ports.output;

import org.springframework.security.core.Authentication;
import pe.com.graduate.insights.api.features.auth.domain.model.AuthPrincipal;

public interface AuthRepositoryPort {
  AuthPrincipal login(String email, String password);

  AuthPrincipal getCurrentUser(Authentication authentication);
}
