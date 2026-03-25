package pe.com.graduate.insights.api.features.emailconfig.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigRequest;
import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigResponse;
import pe.com.graduate.insights.api.features.emailconfig.application.ports.output.EmailConfigRepositoryPort;
import pe.com.graduate.insights.api.features.emailconfig.infrastructure.entity.EmailConfigEntity;
import pe.com.graduate.insights.api.features.emailconfig.infrastructure.jpa.EmailConfigRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConfigRepositoryAdapter implements EmailConfigRepositoryPort {

  private final EmailConfigRepository emailConfigRepository;

  @Override
  public EmailConfigResponse getActiveConfig() {
    return emailConfigRepository
        .findByActivoTrue()
        .map(
            entity ->
                EmailConfigResponse.builder()
                    .id(entity.getId())
                    .proveedor(entity.getProveedor())
                    .emailRemitente(entity.getEmailRemitente())
                    .nombreRemitente(entity.getNombreRemitente())
                    .activo(entity.getActivo())
                    .build())
        .orElse(null);
  }

  @Override
  public void saveOrUpdate(EmailConfigRequest request) {
    EmailConfigEntity entity =
        emailConfigRepository.findByActivoTrue().orElse(new EmailConfigEntity());
    entity.setProveedor("resend");
    entity.setApiKey(request.getApiKey());
    entity.setEmailRemitente(request.getEmailRemitente());
    entity.setNombreRemitente(request.getNombreRemitente());
    entity.setActivo(true);
    emailConfigRepository.save(entity);
  }

  @Override
  public EmailConfigEntity getActiveEntity() {
    return emailConfigRepository.findByActivoTrue().orElse(null);
  }
}
