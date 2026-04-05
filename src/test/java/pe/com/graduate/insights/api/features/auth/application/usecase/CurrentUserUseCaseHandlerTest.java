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
import pe.com.graduate.insights.api.features.auth.application.ports.output.AuthRepositoryPort;
import pe.com.graduate.insights.api.features.auth.domain.model.AuthPrincipal;
import pe.com.graduate.insights.api.features.auth.domain.model.AuthenticatedUser;

@ExtendWith(MockitoExtension.class)
class CurrentUserUseCaseHandlerTest {

  @Mock private AuthRepositoryPort authRepositoryPort;
  @Mock private Authentication authentication;

  @InjectMocks private CurrentUserUseCaseHandler currentUserUseCaseHandler;

  @Test
  void getCurrentUserShouldDelegateToAuthRepositoryPort() {
    AuthPrincipal principal =
        AuthPrincipal.builder()
            .id(10L)
            .username("jane")
            .firstName("Jane")
            .lastName("Doe")
            .email("jane@example.com")
            .verified(true)
            .build();
    when(authRepositoryPort.getCurrentUser(authentication)).thenReturn(principal);

    AuthenticatedUser user = currentUserUseCaseHandler.getCurrentUser(authentication);

    assertEquals(10L, user.id());
    assertEquals("Jane", user.firstName());
    assertEquals("Doe", user.lastName());
    assertEquals("jane@example.com", user.email());
    assertEquals(true, user.verified());
    verify(authRepositoryPort).getCurrentUser(authentication);
  }
}
