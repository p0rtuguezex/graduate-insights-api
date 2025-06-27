package pe.com.graduate.insights.api.domain.models.request.generic;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GenericIds {
  private String id1;
  private String id2;
}
