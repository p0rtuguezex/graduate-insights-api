package pe.com.graduate.insights.api.domain.exception;

public class AccountPendingApprovalException extends RuntimeException {
  public AccountPendingApprovalException(String message) {
    super(message);
  }
}
