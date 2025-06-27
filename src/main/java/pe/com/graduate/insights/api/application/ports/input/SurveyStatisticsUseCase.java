package pe.com.graduate.insights.api.application.ports.input;

import org.springframework.http.ResponseEntity;
import pe.com.graduate.insights.api.domain.models.response.ChartDataResponse;
import pe.com.graduate.insights.api.domain.models.response.DashboardOverviewResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyStatisticsResponse;

import java.util.List;

public interface SurveyStatisticsUseCase {
    
    /**
     * Obtiene estadísticas completas de una encuesta específica
     */
    SurveyStatisticsResponse getSurveyStatistics(Long surveyId);
    
    /**
     * Obtiene datos formateados para gráficos de una pregunta específica
     */
    ChartDataResponse getQuestionChartData(Long surveyId, Long questionId, String chartType);
    
    /**
     * Obtiene datos para el dashboard general filtrado por año de graduación
     */
    DashboardOverviewResponse getDashboardOverview(Integer graduationYear);
    
    /**
     * Compara estadísticas entre múltiples encuestas
     */
    List<SurveyStatisticsResponse> compareSurveys(List<Long> surveyIds);
    
    /**
     * Obtiene datos de tendencias temporales
     */
    ChartDataResponse getSurveyTrends(Long surveyId, String period);
    
    /**
     * Obtiene datos demográficos para gráficos
     */
    ChartDataResponse getDemographicsData(Long surveyId, String demographic);
    
    /**
     * Exporta datos de encuesta en diferentes formatos
     */
    ResponseEntity<byte[]> exportSurveyData(Long surveyId, String format);
} 