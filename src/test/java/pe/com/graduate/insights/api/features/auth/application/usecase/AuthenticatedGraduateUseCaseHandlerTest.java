package pe.com.graduate.insights.api.features.auth.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import pe.com.graduate.insights.api.features.auth.application.ports.input.CurrentUserUseCase;
import pe.com.graduate.insights.api.features.auth.domain.model.AuthenticatedUser;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateIdentityRepositoryPort;

@ExtendWith(MockitoExtension.class)
class AuthenticatedGraduateUseCaseHandlerTest {

  @Mock private CurrentUserUseCase currentUserUseCase;
  @Mock private GraduateIdentityRepositoryPort graduateIdentityRepositoryPort;
  @Mock private Authentication authentication;

  @InjectMocks private AuthenticatedGraduateUseCaseHandler authenticatedGraduateUseCaseHandler;

  @Test
  void getAuthenticatedGraduateIdShouldResolveFromCurrentUser() {
    AuthenticatedUser user =
        AuthenticatedUser.builder()
            .id(7L)
            .firstName("Jane")
            .lastName("Doe")
            .email("jane@example.com")
            .verified(true)
            .build();

    when(currentUserUseCase.getCurrentUser(authentication)).thenReturn(user);
    when(graduateIdentityRepositoryPort.getActiveGraduateIdByUserId(7L)).thenReturn(99L);

    Long graduateId = authenticatedGraduateUseCaseHandler.getAuthenticatedGraduateId(authentication);

    assertEquals(99L, graduateId);
    verify(currentUserUseCase).getCurrentUser(authentication);
    verify(graduateIdentityRepositoryPort).getActiveGraduateIdByUserId(7L);
  }
}
