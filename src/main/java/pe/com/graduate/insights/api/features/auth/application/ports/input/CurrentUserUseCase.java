package pe.com.graduate.insights.api.features.auth.application.ports.input;

import org.springframework.security.core.Authentication;
import pe.com.graduate.insights.api.features.auth.domain.model.AuthenticatedUser;

public interface CurrentUserUseCase {
  AuthenticatedUser getCurrentUser(Authentication authentication);
}
