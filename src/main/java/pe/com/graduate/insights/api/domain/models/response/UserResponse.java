package pe.com.graduate.insights.api.domain.models.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class UserResponse {

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
}
