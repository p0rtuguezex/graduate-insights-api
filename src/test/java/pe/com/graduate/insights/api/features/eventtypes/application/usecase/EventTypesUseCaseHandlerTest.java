package pe.com.graduate.insights.api.features.eventtypes.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.eventtypes.application.dto.EventTypesResponse;
import pe.com.graduate.insights.api.features.eventtypes.application.ports.output.EventTypesRepositoryPort;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;

@ExtendWith(MockitoExtension.class)
class EventTypesUseCaseHandlerTest {

  @Mock private EventTypesRepositoryPort eventTypesRepositoryPort;

  @InjectMocks private EventTypesUseCaseHandler eventTypesUseCaseHandler;

  @Test
  void getListShouldDelegateToRepositoryPort() {
    List<KeyValueResponse> expected = List.of();
    when(eventTypesRepositoryPort.getList()).thenReturn(expected);

    List<KeyValueResponse> result = eventTypesUseCaseHandler.getList();

    assertSame(expected, result);
    verify(eventTypesRepositoryPort).getList();
  }

  @Test
  void getDomainShouldDelegateToRepositoryPort() {
    EventTypesResponse expected = EventTypesResponse.builder().build();
    when(eventTypesRepositoryPort.getDomain(10L)).thenReturn(expected);

    EventTypesResponse result = eventTypesUseCaseHandler.getDomain(10L);

    assertSame(expected, result);
    verify(eventTypesRepositoryPort).getDomain(10L);
  }
}
