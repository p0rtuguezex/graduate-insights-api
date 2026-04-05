package pe.com.graduate.insights.api.features.surveytype.application.dto;

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
