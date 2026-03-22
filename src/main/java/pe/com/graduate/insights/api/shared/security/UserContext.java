package pe.com.graduate.insights.api.shared.security;

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
