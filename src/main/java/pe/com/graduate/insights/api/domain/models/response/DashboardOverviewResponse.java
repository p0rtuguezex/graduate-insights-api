package pe.com.graduate.insights.api.domain.models.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverviewResponse {

  // Estadísticas generales
  private GeneralStatistics generalStatistics;

  // Datos para gráficos del dashboard
  private List<ChartDataResponse> dashboardCharts;

  // Encuestas más recientes
  private List<SurveyResponse> recentSurveys;

  // Indicadores clave de rendimiento (KPIs)
  private List<KpiIndicator> kpiIndicators;

  // Filtros aplicados
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
    private String trend; // "up", "down", "stable"
    private Double changePercentage;
    private String description;
    private String color; // Para styling en el frontend
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
