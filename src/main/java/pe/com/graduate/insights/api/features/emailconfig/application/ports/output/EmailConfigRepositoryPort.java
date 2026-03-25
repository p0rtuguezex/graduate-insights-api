package pe.com.graduate.insights.api.features.emailconfig.application.ports.output;

import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigRequest;
import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigResponse;
import pe.com.graduate.insights.api.features.emailconfig.infrastructure.entity.EmailConfigEntity;

public interface EmailConfigRepositoryPort {

  EmailConfigResponse getActiveConfig();

  void saveOrUpdate(EmailConfigRequest request);

  EmailConfigEntity getActiveEntity();
}
