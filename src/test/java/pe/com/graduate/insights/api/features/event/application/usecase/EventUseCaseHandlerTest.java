package pe.com.graduate.insights.api.features.event.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.event.application.dto.EventResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.features.event.application.ports.output.EventRepositoryPort;

@ExtendWith(MockitoExtension.class)
class EventUseCaseHandlerTest {

  @Mock private EventRepositoryPort eventRepositoryPort;

  @InjectMocks private EventUseCaseHandler eventUseCaseHandler;

  @Test
  void getListShouldDelegateToRepositoryPort() {
    List<KeyValueResponse> expected = List.of();
    when(eventRepositoryPort.getList()).thenReturn(expected);

    List<KeyValueResponse> result = eventUseCaseHandler.getList();

    assertSame(expected, result);
    verify(eventRepositoryPort).getList();
  }

  @Test
  void getDomainShouldDelegateToRepositoryPort() {
    EventResponse expected = EventResponse.builder().build();
    when(eventRepositoryPort.getDomain(11L)).thenReturn(expected);

    EventResponse result = eventUseCaseHandler.getDomain(11L);

    assertSame(expected, result);
    verify(eventRepositoryPort).getDomain(11L);
  }
}


