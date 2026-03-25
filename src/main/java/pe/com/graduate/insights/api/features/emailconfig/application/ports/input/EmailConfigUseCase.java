package pe.com.graduate.insights.api.features.emailconfig.application.ports.input;

import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigRequest;
import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigResponse;

public interface EmailConfigUseCase {

  EmailConfigResponse getActiveConfig();

  void saveOrUpdate(EmailConfigRequest request);

  void sendTestEmail(String toEmail);
}
