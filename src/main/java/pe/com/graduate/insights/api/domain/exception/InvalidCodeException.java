package pe.com.graduate.insights.api.domain.exception;

public class InvalidCodeException extends RuntimeException {

  public InvalidCodeException(String message) {
    super(message);
  }
}
