package pe.com.graduate.insights.api.infrastructure.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class CorsConfig {

  private final CorsProperties properties;

  public CorsConfig(CorsProperties properties) {
    this.properties = properties;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    apply(
        configuration::setAllowedOrigins,
        defaultIfEmpty(properties.getAllowedOrigins(), List.of("http://localhost:3000")));
    apply(
        configuration::setAllowedMethods,
        defaultIfEmpty(
            properties.getAllowedMethods(),
            Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")));
    apply(
        configuration::setAllowedHeaders,
        defaultIfEmpty(properties.getAllowedHeaders(), List.of("*")));
    apply(configuration::setExposedHeaders, properties.getExposedHeaders());

    configuration.setAllowCredentials(Boolean.TRUE.equals(properties.getAllowCredentials()));
    if (properties.getMaxAge() != null) {
      configuration.setMaxAge(properties.getMaxAge());
    }

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  private void apply(java.util.function.Consumer<List<String>> setter, List<String> values) {
    if (!CollectionUtils.isEmpty(values)) {
      setter.accept(values);
    }
  }

  private List<String> defaultIfEmpty(List<String> values, List<String> fallback) {
    if (CollectionUtils.isEmpty(values)) {
      return fallback;
    }
    return values;
  }
}
