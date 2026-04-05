package pe.com.graduate.insights.api.features.event.domain.exception;

public class EventException extends RuntimeException {

  public EventException(String detail) {
    super(detail);
  }
}
