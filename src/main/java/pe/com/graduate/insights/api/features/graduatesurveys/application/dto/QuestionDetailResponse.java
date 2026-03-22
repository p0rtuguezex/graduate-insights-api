package pe.com.graduate.insights.api.features.graduatesurveys.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.features.survey.application.dto.QuestionOptionResponse;
import pe.com.graduate.insights.api.features.survey.domain.model.QuestionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailResponse {
  private Long questionId;
  private String questionText;
  private QuestionType questionType;
  private boolean required;
  private List<QuestionOptionResponse> options;

  private List<Long> selectedOptionIds;
  private String textResponse;
  private Integer numericResponse;
  private boolean answered;
}

