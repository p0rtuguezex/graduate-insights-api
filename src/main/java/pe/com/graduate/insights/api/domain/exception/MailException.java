package pe.com.graduate.insights.api.domain.exception;

public class MailException extends RuntimeException {

  public MailException(String detail) {
    super(detail);
  }
}
