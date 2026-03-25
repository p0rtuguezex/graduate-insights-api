package pe.com.graduate.insights.api.features.emailconfig.application.dto;

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
public class EmailConfigResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Long id;
  private String proveedor;
  private String emailRemitente;
  private String nombreRemitente;
  private Boolean activo;
  // NOTE: apiKey is intentionally NOT included for security
}
