package pe.com.graduate.insights.api.infrastructure.adapter.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombres;
  private String apellidos;
  private LocalDate fechaNacimiento;
  private String genero;
  private String correo;
  private String dni;
  private String celular;
  private String contrasena;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
