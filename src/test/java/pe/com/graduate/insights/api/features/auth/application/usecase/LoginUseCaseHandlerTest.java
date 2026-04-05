package pe.com.graduate.insights.api.features.auth.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.auth.application.ports.output.AuthRepositoryPort;
import pe.com.graduate.insights.api.features.auth.application.ports.output.TokenGeneratorPort;
import pe.com.graduate.insights.api.features.auth.domain.model.AuthPrincipal;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseHandlerTest {

  @Mock private AuthRepositoryPort authRepositoryPort;
  @Mock private TokenGeneratorPort tokenGeneratorPort;

  @InjectMocks private LoginUseCaseHandler loginUseCaseHandler;

  @Test
  void loginShouldReturnTokenForValidCredentials() {
    String email = "user@example.com";
    String password = "password123";
    String expectedToken = "jwt-token";

    AuthPrincipal user =
        AuthPrincipal.builder()
            .id(1L)
            .username(email)
            .firstName("Test")
            .lastName("User")
            .email(email)
            .verified(true)
            .build();

    when(authRepositoryPort.login(email, password)).thenReturn(user);
    when(tokenGeneratorPort.generateToken(user.username())).thenReturn(expectedToken);

    String token = loginUseCaseHandler.login(email, password);

    assertEquals(expectedToken, token);
    verify(authRepositoryPort).login(email, password);
    verify(tokenGeneratorPort).generateToken(user.username());
  }
}
