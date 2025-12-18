package pe.com.graduate.insights.api.domain.models.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class ProfileUpdateRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "El nombre no puede ser null")
  @NotBlank(message = "El nombre no puede estar en blanco")
  private String nombres;

  @NotNull(message = "El apellido no puede ser null")
  @NotBlank(message = "El apellido no puede estar en blanco")
  private String apellidos;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonAlias({"fechaNacimiento", "fecha_nacimiento"})
  private LocalDate fechaNacimiento;

  @NotBlank(message = "El género no puede estar en blanco")
  @Pattern(regexp = "^(M|F|Otro)$", message = "El género debe ser M, F u Otro")
  private String genero;

  @NotBlank(message = "El correo no puede estar en blanco")
  @Email(message = "El correo debe ser válido")
  private String correo;

  @NotBlank(message = "El DNI no puede estar en blanco")
  @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 dígitos")
  private String dni;

  @NotBlank(message = "El número de celular no puede estar en blanco")
  @Pattern(
      regexp = "^9\\d{8}$",
      message = "El número de celular debe tener 9 dígitos y comenzar con 9")
  private String celular;
}
