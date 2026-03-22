package pe.com.graduate.insights.api.features.auth.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.auth.application.ports.input.LoginUseCase;
import pe.com.graduate.insights.api.features.auth.application.ports.output.AuthRepositoryPort;
import pe.com.graduate.insights.api.features.auth.application.ports.output.TokenGeneratorPort;

@Service
@RequiredArgsConstructor
public class LoginUseCaseHandler implements LoginUseCase {

  private final AuthRepositoryPort authRepositoryPort;
  private final TokenGeneratorPort tokenGeneratorPort;

  @Override
  public String login(String email, String password) {
    var user = authRepositoryPort.login(email, password);
    return tokenGeneratorPort.generateToken(user.username());
  }
}
