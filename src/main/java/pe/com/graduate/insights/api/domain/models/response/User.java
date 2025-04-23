package pe.com.graduate.insights.api.domain.models.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

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
