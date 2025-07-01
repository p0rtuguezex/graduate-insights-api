package pe.com.graduate.insights.api.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyTypeResponse {
  private Long id;
  private String name;
  private String description;
  private Boolean active;
}
