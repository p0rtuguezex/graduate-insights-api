package pe.com.graduate.insights.api.domain.models.response;

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
public class EventResponse {
  private Long eventId;
  private String nombre;
  private String contenido;
  private String estado;
  private Long directorId;
  private String directorNombre;
  private Long eventTypeId;
  private String eventTypeNombre;
}
