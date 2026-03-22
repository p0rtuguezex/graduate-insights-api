package pe.com.graduate.insights.api.features.mail.application.usecase;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.mail.application.dto.ChangePasswordRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.MailRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.ValidateCodeRequest;
import pe.com.graduate.insights.api.features.mail.application.ports.output.MailRepositoryPort;

@ExtendWith(MockitoExtension.class)
class MailUseCaseHandlerTest {

  @Mock private MailRepositoryPort mailRepositoryPort;

  @InjectMocks private MailUseCaseHandler mailUseCaseHandler;

  @Test
  void sendCodeShouldDelegateToRepositoryPort() {
    MailRequest request = new MailRequest();

    mailUseCaseHandler.sendCode(request);

    verify(mailRepositoryPort).sendCode(request);
  }

  @Test
  void validateCodeShouldDelegateToRepositoryPort() {
    ValidateCodeRequest request = new ValidateCodeRequest();

    mailUseCaseHandler.validateCode(request);

    verify(mailRepositoryPort).validateCode(request);
  }

  @Test
  void changePasswordShouldDelegateToRepositoryPort() {
    ChangePasswordRequest request =
        ChangePasswordRequest.builder().email("user@example.com").code("123456").newPassword("Password1!").build();

    mailUseCaseHandler.changePassword(request);

    verify(mailRepositoryPort).changePassword(request);
  }
}
