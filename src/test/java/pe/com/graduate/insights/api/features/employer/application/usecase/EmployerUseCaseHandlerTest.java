package pe.com.graduate.insights.api.features.employer.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.employer.application.dto.EmployerResponse;
import pe.com.graduate.insights.api.features.employer.application.ports.output.EmployerRepositoryPort;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;

@ExtendWith(MockitoExtension.class)
class EmployerUseCaseHandlerTest {

  @Mock private EmployerRepositoryPort employerRepositoryPort;

  @InjectMocks private EmployerUseCaseHandler employerUseCaseHandler;

  @Test
  void getListShouldDelegateToRepositoryPort() {
    List<KeyValueResponse> expected = List.of();
    when(employerRepositoryPort.getList()).thenReturn(expected);

    List<KeyValueResponse> result = employerUseCaseHandler.getList();

    assertSame(expected, result);
    verify(employerRepositoryPort).getList();
  }

  @Test
  void getDomainShouldDelegateToRepositoryPort() {
    EmployerResponse expected = EmployerResponse.builder().build();
    when(employerRepositoryPort.getDomain(6L)).thenReturn(expected);

    EmployerResponse result = employerUseCaseHandler.getDomain(6L);

    assertSame(expected, result);
    verify(employerRepositoryPort).getDomain(6L);
  }
}
