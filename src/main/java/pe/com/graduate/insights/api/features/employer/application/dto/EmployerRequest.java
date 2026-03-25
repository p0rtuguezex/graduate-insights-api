package pe.com.graduate.insights.api.features.employer.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployerRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  // Contact person data
  @NotBlank(message = "El nombre del contacto es obligatorio")
  private String nombres;

  @NotBlank(message = "El apellido del contacto es obligatorio")
  private String apellidos;

  @NotBlank(message = "El correo es obligatorio")
  @Email(message = "El correo debe ser valido")
  private String correo;

  @NotBlank(message = "El celular es obligatorio")
  @Pattern(regexp = "^9\\d{8}$", message = "El celular debe iniciar con 9 y tener 9 digitos")
  private String celular;

  @NotBlank(message = "La contrasena es obligatoria")
  @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
  private String contrasena;

  // Company data
  @NotBlank(message = "El RUC es obligatorio")
  @Pattern(regexp = "^\\d{11}$", message = "El RUC debe tener 11 digitos")
  private String ruc;

  @NotBlank(message = "La razon social es obligatoria")
  private String razonSocial;

  private String direccion;

  private String resumenEmpresa;
}
