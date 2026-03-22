package pe.com.graduate.insights.api.features.userrole.application.ports.output;

import pe.com.graduate.insights.api.shared.security.UserRole;

public interface UserRoleRepositoryPort {
  UserRole getUserRole(Long userId);

  String getUserRoleDisplayName(Long userId);

  String getUserRoleAuthority(Long userId);
}

