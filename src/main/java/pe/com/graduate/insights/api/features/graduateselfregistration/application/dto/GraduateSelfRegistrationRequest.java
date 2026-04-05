package pe.com.graduate.insights.api.features.graduateselfregistration.application.dto;

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
  @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 digitos")
  private String dni;

  @NotBlank(message = "El numero de celular es obligatorio")
  @Pattern(
      regexp = "^9\\d{8}$",
      message = "El numero de celular debe iniciar con 9 y tener 9 digitos")
  private String celular;

  @NotBlank(message = "El correo electronico es obligatorio")
  @Email(message = "El correo electronico tiene un formato invalido")
  private String correo;

  @NotBlank(message = "La contrasena es obligatoria")
  @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
  private String contrasena;
}
