package pe.com.graduate.insights.api.domain.models.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionStatistics {
  private Long questionId;
  private String questionText;
  private QuestionType type;
  private Map<String, Long> optionCounts; // Para gráficos de barras
  private Double average; // Para preguntas de escala
  private Long totalResponses;
  private Map<String, Double> percentages; // Porcentajes para cada opción
}
