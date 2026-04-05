package pe.com.graduate.insights.api.features.userrole.application.ports.input;

import pe.com.graduate.insights.api.shared.security.UserRole;

public interface UserRoleUseCase {
  UserRole getUserRole(Long userId);

  String getUserRoleDisplayName(Long userId);

  String getUserRoleAuthority(Long userId);
}
