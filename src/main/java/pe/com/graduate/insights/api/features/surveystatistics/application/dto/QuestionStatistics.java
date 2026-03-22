package pe.com.graduate.insights.api.features.surveystatistics.application.dto;

import java.util.List;
import java.util.Map;
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
public class QuestionStatistics {
  private Long questionId;
  private String questionText;
  private QuestionType type;
  private Boolean required;
  private Integer orderNumber;

  private Long totalResponses;
  private Long skippedResponses;
  private Double responseRate;

  private Map<String, Long> optionCounts;
  private Map<String, Double> percentages;
  private List<QuestionOptionResponse> optionDetails;

  private Double average;
  private Double median;
  private Double mode;
  private Double standardDeviation;
  private Double min;
  private Double max;
  private Map<String, Long> distribution;

  private List<String> commonKeywords;
  private Integer averageResponseLength;
  private List<String> sampleResponses;

  private ChartDataResponse chartData;
  private String recommendedChartType;

  private Map<String, Object> correlations;
  private Map<String, Object> customAnalysis;
}
