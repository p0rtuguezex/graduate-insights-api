package pe.com.graduate.insights.api.features.mail.domain.exception;

public class MailException extends RuntimeException {

  public MailException(String detail) {
    super(detail);
  }
}