package pe.com.graduate.insights.api.domain.models.request;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class DirectorRequest extends UserRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;
  private Long id;
  private String escuela;
}
