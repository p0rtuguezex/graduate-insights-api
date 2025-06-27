package pe.com.graduate.insights.api.domain.models.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionType;

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
