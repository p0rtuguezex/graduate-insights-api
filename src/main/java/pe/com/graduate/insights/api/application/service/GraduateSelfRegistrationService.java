package pe.com.graduate.insights.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.GraduateSelfRegistrationUseCase;
import pe.com.graduate.insights.api.application.ports.output.GraduateSelfRegistrationRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.GraduateSelfRegistrationRequest;

@Service
@RequiredArgsConstructor
public class GraduateSelfRegistrationService implements GraduateSelfRegistrationUseCase {

  private final GraduateSelfRegistrationRepositoryPort graduateSelfRegistrationRepositoryPort;

  @Override
  public void register(GraduateSelfRegistrationRequest request) {
    graduateSelfRegistrationRepositoryPort.register(request);
  }
}
