package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.domain.models.request.ChangePasswordRequest;
import pe.com.graduate.insights.api.domain.models.request.MailRequest;
import pe.com.graduate.insights.api.domain.models.request.ValidateCodeRequest;

public interface MailUseCase {
  void sendCode(MailRequest mailRequest);

  void validateCode(ValidateCodeRequest validateCodeRequest);

  void changePassword(ChangePasswordRequest changePasswordRequest);
}
