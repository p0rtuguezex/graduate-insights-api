package pe.com.graduate.insights.api.features.notification.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

  private Long id;

  @JsonProperty("usuario_id")
  private Long userId;

  private String titulo;
  private String mensaje;
  private String tipo;
  private boolean leido;

  @JsonProperty("fecha_creacion")
  private String fechaCreacion;
}
