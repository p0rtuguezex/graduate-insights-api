package pe.com.graduate.insights.api.infrastructure.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.SurveyStatisticsUseCase;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.ChartDataResponse;
import pe.com.graduate.insights.api.domain.models.response.DashboardOverviewResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyStatisticsResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;

@RestController
@RequestMapping("/survey-statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
public class SurveyStatisticsController {

  private final SurveyStatisticsUseCase surveyStatisticsUseCase;

  /** Obtiene estadísticas completas de una encuesta específica */
  @GetMapping("/{surveyId}")
  public ResponseEntity<ApiResponse<SurveyStatisticsResponse>> getSurveyStatistics(
      @PathVariable Long surveyId) {
    return ResponseUtils.successResponse(surveyStatisticsUseCase.getSurveyStatistics(surveyId));
  }

  /** Datos específicos para gráficos de una pregunta */
  @GetMapping("/{surveyId}/question/{questionId}/chart")
  public ResponseEntity<ApiResponse<ChartDataResponse>> getQuestionChartData(
      @PathVariable Long surveyId,
      @PathVariable Long questionId,
      @RequestParam(value = "chartType", defaultValue = "bar") String chartType) {
    return ResponseUtils.successResponse(
        surveyStatisticsUseCase.getQuestionChartData(surveyId, questionId, chartType));
  }

  /** Datos para dashboard general (overview) */
  @GetMapping("/dashboard")
  public ResponseEntity<ApiResponse<DashboardOverviewResponse>> getDashboardOverview(
      @RequestParam(value = "graduation_year", required = false) Integer graduationYear) {
    return ResponseUtils.successResponse(
        surveyStatisticsUseCase.getDashboardOverview(graduationYear));
  }

  /** Comparativa entre múltiples encuestas */
  @GetMapping("/compare")
  public ResponseEntity<ApiResponse<List<SurveyStatisticsResponse>>> compareSurveys(
      @RequestParam("surveyIds") List<Long> surveyIds) {
    return ResponseUtils.successResponse(surveyStatisticsUseCase.compareSurveys(surveyIds));
  }

  /** Datos para gráfico de tendencias temporales */
  @GetMapping("/{surveyId}/trends")
  public ResponseEntity<ApiResponse<ChartDataResponse>> getSurveyTrends(
      @PathVariable Long surveyId,
      @RequestParam(value = "period", defaultValue = "monthly") String period) {
    return ResponseUtils.successResponse(surveyStatisticsUseCase.getSurveyTrends(surveyId, period));
  }

  /** Datos demográficos para gráficos */
  @GetMapping("/{surveyId}/demographics")
  public ResponseEntity<ApiResponse<ChartDataResponse>> getDemographicsData(
      @PathVariable Long surveyId,
      @RequestParam(value = "demographic", defaultValue = "location") String demographic) {
    return ResponseUtils.successResponse(
        surveyStatisticsUseCase.getDemographicsData(surveyId, demographic));
  }

  /** Exportar datos para gráficos en formato CSV/Excel */
  @GetMapping("/{surveyId}/export")
  public ResponseEntity<byte[]> exportSurveyData(
      @PathVariable Long surveyId,
      @RequestParam(value = "format", defaultValue = "csv") String format) {
    return surveyStatisticsUseCase.exportSurveyData(surveyId, format);
  }
}
