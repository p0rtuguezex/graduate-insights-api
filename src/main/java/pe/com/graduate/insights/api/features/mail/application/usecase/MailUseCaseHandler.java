package pe.com.graduate.insights.api.features.mail.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.mail.application.dto.ChangePasswordRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.MailRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.ValidateCodeRequest;
import pe.com.graduate.insights.api.features.mail.application.ports.input.MailUseCase;
import pe.com.graduate.insights.api.features.mail.application.ports.output.MailRepositoryPort;

@Service
@RequiredArgsConstructor
public class MailUseCaseHandler implements MailUseCase {

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
