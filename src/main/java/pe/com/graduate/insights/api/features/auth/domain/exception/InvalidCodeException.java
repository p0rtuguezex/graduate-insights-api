package pe.com.graduate.insights.api.features.auth.domain.exception;

public class InvalidCodeException extends RuntimeException {

  public InvalidCodeException(String message) {
    super(message);
  }
}
