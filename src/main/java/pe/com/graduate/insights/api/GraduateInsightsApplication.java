package pe.com.graduate.insights.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition
public class GraduateInsightsApplication {

  public static void main(String[] args) {
    SpringApplication.run(GraduateInsightsApplication.class, args);
  }

  @Bean
  public ApplicationRunner corsConfigLogger(@Value("${cors.allowed-origins:http://localhost:3000}") String allowedOrigins,
                                          @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS,PATCH}") String allowedMethods) {
    return args -> {
      System.out.println("========================================");
      System.out.println("     CONFIGURACIÓN CORS APLICADA");
      System.out.println("========================================");
      System.out.println("🌍 Orígenes permitidos: " + allowedOrigins);
      System.out.println("🔧 Métodos permitidos: " + allowedMethods);
      
      // Verificar variable de entorno
      String envCorsOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
      if (envCorsOrigins != null) {
        System.out.println("✅ Variable de entorno CORS_ALLOWED_ORIGINS detectada: " + envCorsOrigins);
      } else {
        System.out.println("⚠️ Variable de entorno CORS_ALLOWED_ORIGINS no encontrada, usando valor por defecto");
      }
      System.out.println("========================================");
    };
  }
}
