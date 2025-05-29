package pe.com.graduate.insights.api.domain.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class EventTypesResponse {
  private Long eventTypeId;
  private String nombre;
  private String estado;
}
