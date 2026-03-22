package pe.com.graduate.insights.api.features.auth.infrastructure.adapter;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.auth.application.ports.output.AuthRepositoryPort;
import pe.com.graduate.insights.api.features.auth.domain.exception.AccountPendingApprovalException;
import pe.com.graduate.insights.api.features.auth.domain.exception.InvalidCredentialsException;
import pe.com.graduate.insights.api.features.auth.domain.model.AuthPrincipal;
import pe.com.graduate.insights.api.features.userrole.application.ports.output.UserRoleRepositoryPort;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.features.user.infrastructure.jpa.UserRepository;
import pe.com.graduate.insights.api.shared.security.UserRole;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;

@Component
@RequiredArgsConstructor
public class AuthRepositoryAdapter implements AuthRepositoryPort {

  private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRoleRepositoryPort userRoleRepositoryPort;
  private final GraduateRepository graduateRepository;

  @Override
  public AuthPrincipal login(String email, String password) {
    if (!isValidEmail(email)) {
      throw new InvalidCredentialsException("Invalid email format");
    }

    UserEntity user =
        userRepository
            .findByCorreo(email)
            .filter(u -> passwordEncoder.matches(password, u.getContrasena()))
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

    UserRole userRole = userRoleRepositoryPort.getUserRole(user.getId());
    user.setUserRole(userRole);

    if (UserRole.GRADUATE.equals(userRole)) {
      GraduateEntity graduateEntity =
          graduateRepository
              .findByUserIdAndUserEstado(user.getId(), ConstantsUtils.STATUS_ACTIVE)
              .orElseThrow(
                  () ->
                      new InvalidCredentialsException(
                          String.format(ConstantsUtils.USER_NOT_FOUND, user.getId())));

      if (Boolean.FALSE.equals(graduateEntity.getValidated())) {
        throw new AccountPendingApprovalException(ConstantsUtils.GRADUATE_PENDING_APPROVAL);
      }
    }

    return toAuthPrincipal(user);
  }

  @Override
  public AuthPrincipal getCurrentUser(Authentication authentication) {
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }
    UserEntity user =
        userRepository
            .findByCorreo(authentication.getName())
            .orElseThrow(() -> new InvalidCredentialsException("Usuario no encontrado"));

    user.setUserRole(userRoleRepositoryPort.getUserRole(user.getId()));

    return toAuthPrincipal(user);
  }

  private boolean isValidEmail(String email) {
    return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
  }

  private AuthPrincipal toAuthPrincipal(UserEntity user) {
    return AuthPrincipal.builder()
        .id(user.getId())
        .username(user.getUsername())
        .firstName(user.getNombres())
        .lastName(user.getApellidos())
        .email(user.getCorreo())
        .verified(user.isVerificado())
        .build();
  }
}


