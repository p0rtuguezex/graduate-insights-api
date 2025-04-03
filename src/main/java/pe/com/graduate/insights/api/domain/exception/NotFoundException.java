package pe.com.graduate.insights.api.domain.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String detail) {
    super(detail);
  }
}
