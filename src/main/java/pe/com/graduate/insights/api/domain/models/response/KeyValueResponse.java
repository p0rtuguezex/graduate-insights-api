package pe.com.graduate.insights.api.domain.models.response;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString
public class KeyValueResponse {
  private Long key;
  private String value;
}
