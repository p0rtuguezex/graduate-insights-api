package pe.com.graduate.insights.api.features.auth.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.auth.application.ports.input.AuthenticatedGraduateUseCase;
import pe.com.graduate.insights.api.features.auth.application.ports.input.CurrentUserUseCase;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateIdentityRepositoryPort;

@Service
@RequiredArgsConstructor
public class AuthenticatedGraduateUseCaseHandler implements AuthenticatedGraduateUseCase {

  private final CurrentUserUseCase currentUserUseCase;
  private final GraduateIdentityRepositoryPort graduateIdentityRepositoryPort;

  @Override
  public Long getAuthenticatedGraduateId(Authentication authentication) {
    var user = currentUserUseCase.getCurrentUser(authentication);
    return graduateIdentityRepositoryPort.getActiveGraduateIdByUserId(user.id());
  }
}
