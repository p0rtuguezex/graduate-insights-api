package pe.com.graduate.insights.api.features.userrole.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.shared.security.UserRole;
import pe.com.graduate.insights.api.features.userrole.application.ports.input.UserRoleUseCase;
import pe.com.graduate.insights.api.features.userrole.application.ports.output.UserRoleRepositoryPort;

@Service
@RequiredArgsConstructor
public class UserRoleUseCaseHandler implements UserRoleUseCase {

  private final UserRoleRepositoryPort userRoleRepositoryPort;

  @Override
  public UserRole getUserRole(Long userId) {
    return userRoleRepositoryPort.getUserRole(userId);
  }

  @Override
  public String getUserRoleDisplayName(Long userId) {
    return userRoleRepositoryPort.getUserRoleDisplayName(userId);
  }

  @Override
  public String getUserRoleAuthority(Long userId) {
    return userRoleRepositoryPort.getUserRoleAuthority(userId);
  }
}

