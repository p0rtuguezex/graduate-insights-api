package pe.com.graduate.insights.api.features.storage.domain.exception;

public class FileException extends RuntimeException {

  public FileException(String detail) {
    super(detail);
  }

  public FileException(String detail, Throwable cause) {
    super(detail, cause);
  }
}