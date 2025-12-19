package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraduateSelfRegistrationRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @NotBlank(message = "Los nombres son obligatorios")
  @Size(max = 120, message = "Los nombres no deben exceder 120 caracteres")
  private String nombres;

  @NotBlank(message = "Los apellidos son obligatorios")
  @Size(max = 120, message = "Los apellidos no deben exceder 120 caracteres")
  private String apellidos;

  @NotBlank(message = "El DNI es obligatorio")
  @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos")
  private String dni;

  @NotBlank(message = "El número de celular es obligatorio")
  @Pattern(
      regexp = "^9\\d{8}$",
      message = "El número de celular debe iniciar con 9 y tener 9 dígitos")
  private String celular;

  @NotBlank(message = "El correo electrónico es obligatorio")
  @Email(message = "El correo electrónico tiene un formato inválido")
  private String correo;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
  private String contrasena;
}
