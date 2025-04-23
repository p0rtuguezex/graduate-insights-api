package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.*;
import java.io.Serial;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@Setter // revisar si es necesario
public class UserRequest {

  @Serial private static final long serialVersionUID = 1L;

  @NotBlank(message = "El nombre no puede estar vacio")
  @NotNull(message = "El nombre no puede ser nulo")
  private String nombres;

  @NotBlank(message = "El apellido  no puede estar vacio")
  @NotNull(message = "El apellido  no puede ser nulo")
  private String apellidos;

  //    @Pattern(regexp ="^\\d{4}-\\d{2}-\\d{2}$"
  //      ,message ="La fecha de nacimiento debe tener el formato yyy-MM-dd")

  @NotNull(message = "La fecha no puede ser nula")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaNacimiento;

  @NotBlank(message = "El genero no puede estar vacio")
  @NotNull(message = "El genero  no puede ser nulo")
  @Pattern(regexp = "^(M|F|Otro)$", message = "El genero debe ser M,F u otro")
  private String genero;

  @NotBlank(message = "El correo no puede estar vacio")
  @Email(message = "El correo debe ser valido")
  private String correo;

  @NotBlank(message = "El dni no puede estar vacio")
  @NotNull(message = "El dni no puede ser nulo")
  @Pattern(regexp = "^\\d{8}$", message = "El dni debe tener 8 digitos ")
  private String dni;

  @NotBlank(message = "El celular no puede estar vacio")
  @NotNull(message = "El celular no puede ser nulo")
  @Pattern(regexp = "^9\\d{8}$", message = "El celular debe tener 9 digitos y debe empezar con 9 ")
  private String celular;

  @NotBlank(message = "La contraseña no puede estar vacia")
  @NotNull(message = "La contraseña no  puede ser nula")
  @Size(min = 6, message = "La contraseña debe tener como minimo 6 caracteres ")
  private String contrasena;
}
