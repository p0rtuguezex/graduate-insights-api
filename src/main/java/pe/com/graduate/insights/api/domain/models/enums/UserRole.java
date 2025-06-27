package pe.com.graduate.insights.api.domain.models.enums;

public enum UserRole {
  DIRECTOR("ROLE_DIRECTOR", "Director"),
  EMPLOYER("ROLE_EMPLOYER", "Empleador"),
  GRADUATE("ROLE_GRADUATE", "Graduado");

  private final String authority;
  private final String displayName;

  UserRole(String authority, String displayName) {
    this.authority = authority;
    this.displayName = displayName;
  }

  public String getAuthority() {
    return authority;
  }

  public String getDisplayName() {
    return displayName;
  }
}
