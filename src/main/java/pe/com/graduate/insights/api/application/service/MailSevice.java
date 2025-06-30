package pe.com.graduate.insights.api.application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.MailUseCase;
import pe.com.graduate.insights.api.application.ports.output.MailRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.ChangePasswordRequest;
import pe.com.graduate.insights.api.domain.models.request.MailRequest;
import pe.com.graduate.insights.api.domain.models.request.ValidateCodeRequest;

@Service
@AllArgsConstructor
public class MailSevice implements MailUseCase {

  private final MailRepositoryPort mailRepositoryPort;

  @Override
  public void sendCode(MailRequest mailRequest) {
    mailRepositoryPort.sendCode(mailRequest);
  }

  @Override
  public void validateCode(ValidateCodeRequest validateCodeRequest) {
    mailRepositoryPort.validateCode(validateCodeRequest);
  }

  @Override
  public void changePassword(ChangePasswordRequest changePasswordRequest) {
    mailRepositoryPort.changePassword(changePasswordRequest);
  }
}
