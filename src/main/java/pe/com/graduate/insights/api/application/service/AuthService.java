package pe.com.graduate.insights.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.AuthUseCase;
import pe.com.graduate.insights.api.application.ports.output.AuthRepositoryPort;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

  private final AuthRepositoryPort authRepositoryPort;

  @Override
  public UserEntity login(String email, String password) {
    return authRepositoryPort.login(email, password);
  }

  @Override
  public UserEntity getCurrentUser(Authentication authentication) {
    return authRepositoryPort.getCurrentUser(authentication);
  }
}
