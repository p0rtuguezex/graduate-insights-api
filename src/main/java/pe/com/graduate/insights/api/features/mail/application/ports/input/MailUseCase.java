package pe.com.graduate.insights.api.features.mail.application.ports.input;

import pe.com.graduate.insights.api.features.mail.application.dto.ChangePasswordRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.MailRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.ValidateCodeRequest;

public interface MailUseCase {
  void sendCode(MailRequest mailRequest);

  void validateCode(ValidateCodeRequest validateCodeRequest);

  void changePassword(ChangePasswordRequest changePasswordRequest);
}
