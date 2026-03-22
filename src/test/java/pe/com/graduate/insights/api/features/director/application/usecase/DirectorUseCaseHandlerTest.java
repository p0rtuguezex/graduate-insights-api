package pe.com.graduate.insights.api.features.director.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.features.director.application.dto.DirectorResponse;
import pe.com.graduate.insights.api.features.director.application.ports.output.DirectorRepositoryPort;

@ExtendWith(MockitoExtension.class)
class DirectorUseCaseHandlerTest {

  @Mock private DirectorRepositoryPort directorRepositoryPort;

  @InjectMocks private DirectorUseCaseHandler directorUseCaseHandler;

  @Test
  void getListShouldDelegateToRepositoryPort() {
    List<KeyValueResponse> expected = List.of();
    when(directorRepositoryPort.getList()).thenReturn(expected);

    List<KeyValueResponse> result = directorUseCaseHandler.getList();

    assertSame(expected, result);
    verify(directorRepositoryPort).getList();
  }

  @Test
  void getDomainShouldDelegateToRepositoryPort() {
    DirectorResponse expected = DirectorResponse.builder().build();
    when(directorRepositoryPort.getDomain(5L)).thenReturn(expected);

    DirectorResponse result = directorUseCaseHandler.getDomain(5L);

    assertSame(expected, result);
    verify(directorRepositoryPort).getDomain(5L);
  }
}

