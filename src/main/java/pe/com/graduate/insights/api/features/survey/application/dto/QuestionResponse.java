package pe.com.graduate.insights.api.features.survey.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.features.survey.domain.model.QuestionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
  private Long id;
  private String questionText;
  private QuestionType questionType;
  private boolean required;
  private List<QuestionOptionResponse> options;
}
