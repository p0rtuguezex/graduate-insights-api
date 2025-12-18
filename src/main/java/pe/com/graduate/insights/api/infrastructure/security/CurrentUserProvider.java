package pe.com.graduate.insights.api.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.input.AuthUseCase;
import pe.com.graduate.insights.api.application.service.UserRoleService;
import pe.com.graduate.insights.api.domain.models.context.UserContext;
import pe.com.graduate.insights.api.domain.models.enums.UserRole;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

  private final AuthUseCase authUseCase;
  private final UserRoleService userRoleService;

  public UserContext getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = authUseCase.getCurrentUser(authentication);
    UserRole userRole = userRoleService.getUserRole(user.getId());
    return new UserContext(user.getId(), userRole);
  }
}
