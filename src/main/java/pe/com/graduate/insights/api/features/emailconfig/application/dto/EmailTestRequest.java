package pe.com.graduate.insights.api.features.emailconfig.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailTestRequest {

  @NotBlank(message = "El correo destinatario es obligatorio")
  @Email(message = "El correo debe tener un formato valido")
  private String email;
}
