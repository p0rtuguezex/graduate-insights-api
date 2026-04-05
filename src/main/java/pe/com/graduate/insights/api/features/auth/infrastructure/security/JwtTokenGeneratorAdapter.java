package pe.com.graduate.insights.api.features.auth.infrastructure.security;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.auth.application.ports.output.TokenGeneratorPort;
import pe.com.graduate.insights.api.shared.infrastructure.security.JwtService;

@Component
@RequiredArgsConstructor
public class JwtTokenGeneratorAdapter implements TokenGeneratorPort {

  private final JwtService jwtService;

  @Override
  public String generateToken(String username) {
    var userDetails = new User(username, "", Collections.emptyList());
    return jwtService.generateToken(userDetails);
  }
}
