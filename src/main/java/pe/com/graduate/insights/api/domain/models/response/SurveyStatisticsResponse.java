package pe.com.graduate.insights.api.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyStatus;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyStatisticsResponse {
    private Long surveyId;
    private String surveyTitle;
    private String surveyDescription;
    private SurveyType surveyType;
    private SurveyStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer graduationYear;
    private String educationCenterName;
    private Long educationCenterId;
    
    // Fechas importantes
    private LocalDateTime surveyCreatedAt;
    private LocalDateTime lastResponseAt;
    private LocalDateTime dataGeneratedAt;
    
    // Estadísticas generales
    private Integer totalGraduates;
    private Integer totalResponses;
    private Integer pendingResponses;
    private Double responseRate;
    private Double completionRate;
    private Integer totalQuestions;
    private Integer answeredQuestions;
    
    // Estadísticas demográficas
    private Map<String, Long> responsesByLocation;
    private Map<String, Long> responsesByIndustry;
    private Map<String, Long> responsesBySalaryRange;
    private Map<String, Long> responsesByGender;
    private Map<String, Long> responsesByAge;
    private Map<String, Long> responsesByEmploymentStatus;
    
    // Estadísticas temporales para gráficos de tendencias
    private Map<String, Long> responsesByDay;
    private Map<String, Long> responsesByWeek;
    private Map<String, Long> responsesByMonth;
    
    // Datos para gráficos quick
    private List<ChartDataResponse> quickCharts;
    
    // Estadísticas por pregunta
    private List<QuestionStatistics> questionStatistics;
    
    // Metadatos adicionales
    private Map<String, Object> additionalMetrics;
} 