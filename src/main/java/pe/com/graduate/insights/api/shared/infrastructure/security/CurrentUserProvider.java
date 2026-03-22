package pe.com.graduate.insights.api.shared.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.auth.application.ports.input.CurrentUserUseCase;
import pe.com.graduate.insights.api.shared.security.UserContext;
import pe.com.graduate.insights.api.shared.security.UserRole;
import pe.com.graduate.insights.api.features.userrole.application.ports.input.UserRoleUseCase;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

  private final CurrentUserUseCase currentUserUseCase;
  private final UserRoleUseCase userRoleUseCase;

  public UserContext getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = currentUserUseCase.getCurrentUser(authentication);
    UserRole userRole = userRoleUseCase.getUserRole(user.id());
    return new UserContext(user.id(), userRole);
  }
}



