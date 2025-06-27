package pe.com.graduate.insights.api.application.ports.output;

import org.springframework.security.core.Authentication;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;

public interface AuthRepositoryPort {
  UserEntity login(String email, String password);

  UserEntity getCurrentUser(Authentication authentication);
}
