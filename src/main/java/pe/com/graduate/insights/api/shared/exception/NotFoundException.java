package pe.com.graduate.insights.api.shared.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String detail) {
    super(detail);
  }
}
