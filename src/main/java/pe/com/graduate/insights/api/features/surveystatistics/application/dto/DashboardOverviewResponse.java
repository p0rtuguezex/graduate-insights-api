package pe.com.graduate.insights.api.features.surveystatistics.application.dto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverviewResponse {

  private GeneralStatistics generalStatistics;
  private List<ChartDataResponse> dashboardCharts;
  private List<SurveyResponse> recentSurveys;
  private List<KpiIndicator> kpiIndicators;
  private DashboardFilters appliedFilters;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GeneralStatistics {
    private Long totalSurveys;
    private Long totalGraduates;
    private Long totalResponses;
    private Double overallResponseRate;
    private Long activeSurveys;
    private Long completedSurveys;
    private Map<String, Long> responsesByGraduationYear;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class KpiIndicator {
    private String name;
    private String value;
    private String unit;
    private String trend;
    private Double changePercentage;
    private String description;
    private String color;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DashboardFilters {
    private Integer graduationYear;
    private String surveyType;
    private String dateRange;
  }
}
