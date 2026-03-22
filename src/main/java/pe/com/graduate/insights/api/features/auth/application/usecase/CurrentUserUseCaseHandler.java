package pe.com.graduate.insights.api.features.auth.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.auth.application.ports.input.CurrentUserUseCase;
import pe.com.graduate.insights.api.features.auth.application.ports.output.AuthRepositoryPort;
import pe.com.graduate.insights.api.features.auth.domain.model.AuthenticatedUser;

@Service
@RequiredArgsConstructor
public class CurrentUserUseCaseHandler implements CurrentUserUseCase {

  private final AuthRepositoryPort authRepositoryPort;

  @Override
  public AuthenticatedUser getCurrentUser(Authentication authentication) {
    var user = authRepositoryPort.getCurrentUser(authentication);
    return AuthenticatedUser.builder()
        .id(user.id())
        .firstName(user.firstName())
        .lastName(user.lastName())
        .email(user.email())
        .verified(user.verified())
        .build();
  }
}
