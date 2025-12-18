package pe.com.graduate.insights.api.domain.models.context;

import pe.com.graduate.insights.api.domain.models.enums.UserRole;

/**
 * Lightweight representation of the authenticated user that can be passed through the
 * application/use-case layers without leaking security framework types.
 */
public record UserContext(Long userId, UserRole role) {

  public boolean isDirector() {
    return UserRole.DIRECTOR.equals(role);
  }

  public boolean isGraduate() {
    return UserRole.GRADUATE.equals(role);
  }
}
