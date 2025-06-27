package pe.com.graduate.insights.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class GraduateInsightsApplication {

  public static void main(String[] args) {
    SpringApplication.run(GraduateInsightsApplication.class, args);
  }
}
