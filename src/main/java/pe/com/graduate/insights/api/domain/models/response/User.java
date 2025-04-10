package pe.com.graduate.insights.api.domain.models.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
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
}
