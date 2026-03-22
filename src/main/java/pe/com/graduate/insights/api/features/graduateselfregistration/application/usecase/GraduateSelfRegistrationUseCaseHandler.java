package pe.com.graduate.insights.api.features.graduateselfregistration.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.graduateselfregistration.application.dto.GraduateSelfRegistrationRequest;
import pe.com.graduate.insights.api.features.graduateselfregistration.application.ports.input
    .GraduateSelfRegistrationUseCase;
import pe.com.graduate.insights.api.features.graduateselfregistration.application.ports.output
    .GraduateSelfRegistrationRepositoryPort;

@Service
@RequiredArgsConstructor
public class GraduateSelfRegistrationUseCaseHandler implements GraduateSelfRegistrationUseCase {

  private final GraduateSelfRegistrationRepositoryPort graduateSelfRegistrationRepositoryPort;

  @Override
  public void register(GraduateSelfRegistrationRequest request) {
    graduateSelfRegistrationRepositoryPort.register(request);
  }
}
