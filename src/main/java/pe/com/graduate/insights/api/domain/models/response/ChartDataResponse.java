package pe.com.graduate.insights.api.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataResponse {
    
    // Información básica
    private String chartType; // "bar", "pie", "line", "doughnut", "radar", "scatter", "area"
    private String title;
    private String subtitle;
    
    // Datos principales
    private List<String> labels; // Etiquetas del eje X o categorías
    private List<ChartDataset> datasets; // Datos de series
    
    // Configuración adicional para el frontend
    private ChartConfiguration configuration;
    
    // Metadatos
    private Long totalResponses;
    private Map<String, Object> metadata; // Datos adicionales flexibles
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartDataset {
        private String label; // Nombre de la serie
        private List<Object> data; // Valores (pueden ser números, strings, etc.)
        private String backgroundColor;
        private String borderColor;
        private Integer borderWidth;
        private List<String> backgroundColors; // Para gráficos con múltiples colores
        private Map<String, Object> additionalProperties; // Propiedades específicas del tipo de gráfico
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartConfiguration {
        private Boolean responsive;
        private Boolean maintainAspectRatio;
        private ChartAxes axes;
        private ChartLegend legend;
        private ChartTooltip tooltip;
        private Map<String, Object> plugins;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ChartAxes {
            private ChartAxis xAxis;
            private ChartAxis yAxis;
            
            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ChartAxis {
                private String type; // "linear", "category", "time", etc.
                private Boolean display;
                private String title;
                private Object min;
                private Object max;
                private Integer stepSize;
            }
        }
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ChartLegend {
            private Boolean display;
            private String position; // "top", "bottom", "left", "right"
        }
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ChartTooltip {
            private Boolean enabled;
            private String mode; // "point", "nearest", "index", etc.
        }
    }
} 