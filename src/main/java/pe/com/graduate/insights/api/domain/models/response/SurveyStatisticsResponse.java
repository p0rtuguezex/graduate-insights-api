package pe.com.graduate.insights.api.domain.models.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyStatisticsResponse {
  private Long surveyId;
  private String surveyTitle;
  private SurveyType surveyType;
  private Integer graduationYear;
  private String educationCenterName;

  // Estadísticas generales
  private Integer totalGraduates;
  private Integer totalResponses;
  private Double responseRate;

  // Estadísticas demográficas
  private Map<String, Long> responsesByLocation;
  private Map<String, Long> responsesByIndustry;
  private Map<String, Long> responsesBySalaryRange;

  private List<QuestionStatistics> questionStatistics;
}
