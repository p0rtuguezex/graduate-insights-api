package pe.com.graduate.insights.api.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionType;

import java.util.List;
import java.util.Map;

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
    
    // Estadísticas básicas
    private Long totalResponses;
    private Long skippedResponses;
    private Double responseRate;
    
    // Para preguntas de opción múltiple/única
    private Map<String, Long> optionCounts;  // Conteos absolutos
    private Map<String, Double> percentages;  // Porcentajes para cada opción
    private List<QuestionOptionResponse> optionDetails; // Detalles de cada opción
    
    // Para preguntas numéricas/escalas
    private Double average;
    private Double median;
    private Double mode;
    private Double standardDeviation;
    private Double min;
    private Double max;
    private Map<String, Long> distribution; // Distribución de valores
    
    // Para preguntas de texto libre
    private List<String> commonKeywords; // Palabras más comunes
    private Integer averageResponseLength;
    private List<String> sampleResponses; // Respuestas de muestra (max 5)
    
    // Para gráficos específicos
    private ChartDataResponse chartData; // Datos pre-formateados para gráficos
    private String recommendedChartType; // Tipo de gráfico recomendado
    
    // Análisis adicional
    private Map<String, Object> correlations; // Correlaciones con otras preguntas
    private Map<String, Object> customAnalysis; // Análisis específico por tipo
} 