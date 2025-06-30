package pe.com.graduate.insights.api.application.ports.output;

import pe.com.graduate.insights.api.domain.models.request.ChangePasswordRequest;
import pe.com.graduate.insights.api.domain.models.request.MailRequest;
import pe.com.graduate.insights.api.domain.models.request.ValidateCodeRequest;

public interface MailRepositoryPort {
  void sendCode(MailRequest mailRequest);

  void validateCode(ValidateCodeRequest validateCodeRequest);

  void changePassword(ChangePasswordRequest changePasswordRequest);
}
