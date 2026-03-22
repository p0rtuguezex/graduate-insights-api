package pe.com.graduate.insights.api.features.surveystatistics.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ChartDataResponse {

  private String chartType;
  private String title;
  private String subtitle;

  private List<String> labels;
  private List<ChartDataset> datasets;

  private ChartConfiguration configuration;

  private Long totalResponses;
  private Map<String, Object> metadata;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChartDataset {
    private String label;
    private List<Object> data;
    private String backgroundColor;
    private String borderColor;
    private Integer borderWidth;
    private List<String> backgroundColors;
    private Map<String, Object> additionalProperties;
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
      @JsonProperty("xAxis")
      private ChartAxis horizontalAxis;

      @JsonProperty("yAxis")
      private ChartAxis verticalAxis;

      @Data
      @Builder
      @NoArgsConstructor
      @AllArgsConstructor
      public static class ChartAxis {
        private String type;
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
      private String position;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartTooltip {
      private Boolean enabled;
      private String mode;
    }
  }
}