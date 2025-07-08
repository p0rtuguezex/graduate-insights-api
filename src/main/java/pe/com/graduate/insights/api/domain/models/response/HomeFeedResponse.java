package pe.com.graduate.insights.api.domain.models.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeFeedResponse {
  private Long id;
  private String tipo; // "EVENT" o "JOB_OFFER"
  private String titulo;
  private String descripcion;
  private String contenido;
  private LocalDateTime fechaCreacion;
  private String fuente; // Nombre del director o empleador
  private String estado;

  // Campos específicos de eventos
  private String tipoEvento;

  // Campos específicos de ofertas laborales
  private String empresa;
  private String salario;
  private String ubicacion;
}
