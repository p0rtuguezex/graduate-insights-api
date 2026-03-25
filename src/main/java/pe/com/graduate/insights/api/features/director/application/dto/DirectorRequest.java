package pe.com.graduate.insights.api.features.director.application.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;

@SuperBuilder
@Getter
@NoArgsConstructor
@ToString
public class DirectorRequest extends UserRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private String cargo;
}