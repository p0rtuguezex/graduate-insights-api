package pe.com.graduate.insights.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.UserRoleUseCase;
import pe.com.graduate.insights.api.application.ports.output.UserRoleRepositoryPort;
import pe.com.graduate.insights.api.domain.models.enums.UserRole;

@Service
@RequiredArgsConstructor
public class UserRoleService implements UserRoleUseCase {

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
