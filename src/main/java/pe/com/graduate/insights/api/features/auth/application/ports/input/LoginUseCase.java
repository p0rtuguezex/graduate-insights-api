package pe.com.graduate.insights.api.features.auth.application.ports.input;

public interface LoginUseCase {
  String login(String email, String password);
}
