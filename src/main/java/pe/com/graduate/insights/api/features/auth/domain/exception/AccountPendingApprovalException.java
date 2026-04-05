package pe.com.graduate.insights.api.features.auth.domain.exception;

public class AccountPendingApprovalException extends RuntimeException {
  public AccountPendingApprovalException(String message) {
    super(message);
  }
}
