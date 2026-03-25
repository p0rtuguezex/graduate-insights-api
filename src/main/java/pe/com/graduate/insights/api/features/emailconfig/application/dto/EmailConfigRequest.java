package pe.com.graduate.insights.api.features.emailconfig.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfigRequest {

  @NotBlank(message = "La API Key es obligatoria")
  private String apiKey;

  @NotBlank(message = "El email remitente es obligatorio")
  @Email(message = "El email remitente debe ser valido")
  private String emailRemitente;

  @NotBlank(message = "El nombre del remitente es obligatorio")
  private String nombreRemitente;
}
