package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.domain.models.enums.UserRole;

public interface UserRoleUseCase {

  UserRole getUserRole(Long userId);

  String getUserRoleDisplayName(Long userId);

  String getUserRoleAuthority(Long userId);
}
