package pe.com.graduate.insights.api.domain.models.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString
public class UserResponse {
  private Long userId;
  private String nombres;
  private String apellidos;
  private LocalDate fechaNacimiento;
  private String genero;
  private String correo;
  private String estado;
  private String dni;
  private String celular;
  private String contrasena;
}
