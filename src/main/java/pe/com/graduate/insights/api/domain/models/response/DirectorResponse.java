package pe.com.graduate.insights.api.domain.models.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@SuperBuilder
public class DirectorResponse extends UserResponse {
  private Long directorId;
  private String escuela;
}
