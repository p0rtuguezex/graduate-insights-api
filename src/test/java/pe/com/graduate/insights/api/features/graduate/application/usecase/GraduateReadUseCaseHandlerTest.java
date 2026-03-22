package pe.com.graduate.insights.api.features.graduate.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GraduateReadUseCaseHandlerTest {

  @Mock private GraduateReadRepositoryPort graduateReadRepositoryPort;

  @InjectMocks private GraduateReadUseCaseHandler graduateReadUseCaseHandler;

  @Test
  void getDomainShouldDelegateToReadRepositoryPort() {
    GraduateResponse expected = GraduateResponse.builder().build();
    when(graduateReadRepositoryPort.getDomain(42L)).thenReturn(expected);

    GraduateResponse result = graduateReadUseCaseHandler.getDomain(42L);

    assertSame(expected, result);
    verify(graduateReadRepositoryPort).getDomain(42L);
  }

  @Test
  void getPaginationWithValidatedShouldDelegateToReadRepositoryPort() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<GraduateResponse> expected = new PageImpl<>(List.of(), pageable, 0);
    when(graduateReadRepositoryPort.getPagination("ana", pageable, Boolean.TRUE))
        .thenReturn(expected);

    Page<GraduateResponse> result =
        graduateReadUseCaseHandler.getPagination("ana", pageable, Boolean.TRUE);

    assertSame(expected, result);
    verify(graduateReadRepositoryPort).getPagination("ana", pageable, Boolean.TRUE);
  }

  @Test
  void getListShouldDelegateToReadRepositoryPort() {
    List<KeyValueResponse> expected = List.of();
    when(graduateReadRepositoryPort.getList()).thenReturn(expected);

    List<KeyValueResponse> result = graduateReadUseCaseHandler.getList();

    assertSame(expected, result);
    verify(graduateReadRepositoryPort).getList();
  }
}

