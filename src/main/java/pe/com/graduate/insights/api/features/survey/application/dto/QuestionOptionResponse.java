package pe.com.graduate.insights.api.features.survey.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionResponse {
  private Long id;
  private String optionText;
  private Integer orderNumber;
}
