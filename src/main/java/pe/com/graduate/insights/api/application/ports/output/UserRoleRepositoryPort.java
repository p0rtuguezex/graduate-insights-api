package pe.com.graduate.insights.api.application.ports.output;

import pe.com.graduate.insights.api.domain.models.enums.UserRole;

public interface UserRoleRepositoryPort {
  UserRole getUserRole(Long userId);

  String getUserRoleDisplayName(Long userId);

  String getUserRoleAuthority(Long userId);
}
