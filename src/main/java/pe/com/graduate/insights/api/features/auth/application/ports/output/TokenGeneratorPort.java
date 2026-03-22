package pe.com.graduate.insights.api.features.auth.application.ports.output;

public interface TokenGeneratorPort {
  String generateToken(String username);
}
