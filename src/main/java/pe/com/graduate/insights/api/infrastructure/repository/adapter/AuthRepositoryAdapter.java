package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.AuthRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.InvalidCredentialsException;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.UserRepository;

@Component
@RequiredArgsConstructor
public class AuthRepositoryAdapter implements AuthRepositoryPort {

  private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRoleRepositoryAdapter userRoleRepositoryAdapter;

  @Override
  public UserEntity login(String email, String password) {
    if (!isValidEmail(email)) {
      throw new InvalidCredentialsException("Invalid email format");
    }

    UserEntity user =
        userRepository
            .findByCorreo(email)
            .filter(u -> passwordEncoder.matches(password, u.getContrasena()))
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

    user.setUserRole(userRoleRepositoryAdapter.getUserRole(user.getId()));

    return user;
  }

  @Override
  public UserEntity getCurrentUser(Authentication authentication) {
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }
    UserEntity user =
        userRepository
            .findByCorreo(authentication.getName())
            .orElseThrow(() -> new InvalidCredentialsException("Usuario no encontrado"));

    user.setUserRole(userRoleRepositoryAdapter.getUserRole(user.getId()));

    return user;
  }

  private boolean isValidEmail(String email) {
    return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
  }
}
