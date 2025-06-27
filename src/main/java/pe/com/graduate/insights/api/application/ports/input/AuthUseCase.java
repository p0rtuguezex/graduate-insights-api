package pe.com.graduate.insights.api.application.ports.input;

import org.springframework.security.core.Authentication;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;

public interface AuthUseCase {
  UserEntity login(String email, String password);

  UserEntity getCurrentUser(Authentication authentication);
}
