package pe.com.graduate.insights.api.features.graduateselfregistration.application.usecase;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.graduateselfregistration.application.dto.GraduateSelfRegistrationRequest;
import pe.com.graduate.insights.api.features.graduateselfregistration.application.ports.output
    .GraduateSelfRegistrationRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GraduateSelfRegistrationUseCaseHandlerTest {

  @Mock private GraduateSelfRegistrationRepositoryPort graduateSelfRegistrationRepositoryPort;

  @InjectMocks
  private GraduateSelfRegistrationUseCaseHandler graduateSelfRegistrationUseCaseHandler;

  @Test
  void registerShouldDelegateToRepositoryPort() {
    GraduateSelfRegistrationRequest request = GraduateSelfRegistrationRequest.builder().build();

    graduateSelfRegistrationUseCaseHandler.register(request);

    verify(graduateSelfRegistrationRepositoryPort).register(request);
  }
}
