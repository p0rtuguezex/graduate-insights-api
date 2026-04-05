package pe.com.graduate.insights.api.shared.infrastructure.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@Getter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
  private List<String> allowedOrigins = new ArrayList<>();
  private List<String> allowedMethods = new ArrayList<>();
  private List<String> allowedHeaders = new ArrayList<>();
  private List<String> exposedHeaders = new ArrayList<>();
  private Boolean allowCredentials = Boolean.TRUE;
  private Long maxAge = 3600L;

  public void setAllowedOrigins(List<String> allowedOrigins) {
    this.allowedOrigins = normalizeList(allowedOrigins);
  }

  public void setAllowedOrigins(String allowedOrigins) {
    this.allowedOrigins = parseCommaSeparated(allowedOrigins);
  }

  public void setAllowedMethods(List<String> allowedMethods) {
    this.allowedMethods = normalizeList(allowedMethods);
  }

  public void setAllowedMethods(String allowedMethods) {
    this.allowedMethods = parseCommaSeparated(allowedMethods);
  }

  public void setAllowedHeaders(List<String> allowedHeaders) {
    this.allowedHeaders = normalizeList(allowedHeaders);
  }

  public void setAllowedHeaders(String allowedHeaders) {
    this.allowedHeaders = parseCommaSeparated(allowedHeaders);
  }

  public void setExposedHeaders(List<String> exposedHeaders) {
    this.exposedHeaders = normalizeList(exposedHeaders);
  }

  public void setExposedHeaders(String exposedHeaders) {
    this.exposedHeaders = parseCommaSeparated(exposedHeaders);
  }

  public void setAllowCredentials(Boolean allowCredentials) {
    this.allowCredentials = allowCredentials;
  }

  public void setMaxAge(Long maxAge) {
    this.maxAge = maxAge;
  }

  private List<String> normalizeList(List<String> values) {
    if (values == null) {
      return new ArrayList<>();
    }
    return values.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(StringUtils::hasText)
        .collect(Collectors.toList());
  }

  private List<String> parseCommaSeparated(String value) {
    if (!StringUtils.hasText(value)) {
      return new ArrayList<>();
    }
    return Arrays.stream(value.split(","))
        .map(String::trim)
        .filter(StringUtils::hasText)
        .collect(Collectors.toList());
  }
}
