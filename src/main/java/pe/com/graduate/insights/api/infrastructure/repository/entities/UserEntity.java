package pe.com.graduate.insights.api.infrastructure.repository.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.com.graduate.insights.api.domain.models.enums.UserRole;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends Auditable implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombres;
  private String apellidos;
  private LocalDate fechaNacimiento;
  private String genero;
  private String correo;
  private String estado;
  private String dni;
  private String celular;
  private String contrasena;

  @Transient private UserRole userRole;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (userRole != null) {
      return List.of(new SimpleGrantedAuthority(userRole.getAuthority()));
    }
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  public String getNombreCompleto() {
    return nombres + " " + apellidos;
  }

  @Override
  public String getPassword() {
    return contrasena;
  }

  @Override
  public String getUsername() {
    return correo;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
