package pe.com.graduate.insights.api.features.graduatesurveys.domain.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GraduateQuestionResponse {
  Long id;
  Long questionId;
  List<Long> selectedOptionIds;
  String textResponse;
  Integer numericResponse;
}