package pe.com.graduate.insights.api.features.surveystatistics.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.features.surveytype.application.dto.SurveyTypeResponse;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyStatisticsResponse {
  private Long surveyId;
  private String surveyTitle;
  private String surveyDescription;
  private SurveyTypeResponse surveyType;
  private SurveyStatus status;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer graduationYear;
  private String educationCenterName;
  private Long educationCenterId;

  private LocalDateTime surveyCreatedAt;
  private LocalDateTime lastResponseAt;
  private LocalDateTime dataGeneratedAt;

  private Integer totalGraduates;
  private Integer totalResponses;
  private Integer pendingResponses;
  private Double responseRate;
  private Double completionRate;
  private Integer totalQuestions;
  private Integer answeredQuestions;

  private Map<String, Long> responsesByLocation;
  private Map<String, Long> responsesByIndustry;
  private Map<String, Long> responsesBySalaryRange;
  private Map<String, Long> responsesByGender;
  private Map<String, Long> responsesByAge;
  private Map<String, Long> responsesByEmploymentStatus;

  private Map<String, Long> responsesByDay;
  private Map<String, Long> responsesByWeek;
  private Map<String, Long> responsesByMonth;

  private List<ChartDataResponse> quickCharts;
  private List<QuestionStatistics> questionStatistics;

  private Map<String, Object> additionalMetrics;
}
