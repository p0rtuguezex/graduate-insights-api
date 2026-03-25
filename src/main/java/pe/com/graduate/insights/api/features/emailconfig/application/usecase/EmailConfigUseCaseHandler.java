package pe.com.graduate.insights.api.features.emailconfig.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigRequest;
import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigResponse;
import pe.com.graduate.insights.api.features.emailconfig.application.ports.input.EmailConfigUseCase;
import pe.com.graduate.insights.api.features.emailconfig.application.ports.output.EmailConfigRepositoryPort;
import pe.com.graduate.insights.api.features.mail.application.ports.output.MailRepositoryPort;

@Service
@RequiredArgsConstructor
public class EmailConfigUseCaseHandler implements EmailConfigUseCase {

  private final EmailConfigRepositoryPort emailConfigRepositoryPort;
  private final MailRepositoryPort mailRepositoryPort;

  @Override
  public EmailConfigResponse getActiveConfig() {
    return emailConfigRepositoryPort.getActiveConfig();
  }

  @Override
  public void saveOrUpdate(EmailConfigRequest request) {
    emailConfigRepositoryPort.saveOrUpdate(request);
  }

  @Override
  public void sendTestEmail(String toEmail) {
    mailRepositoryPort.sendTestEmail(toEmail);
  }
}
