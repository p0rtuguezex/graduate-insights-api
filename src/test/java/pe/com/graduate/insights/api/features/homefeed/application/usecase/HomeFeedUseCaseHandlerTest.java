package pe.com.graduate.insights.api.features.homefeed.application.usecase;

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
import pe.com.graduate.insights.api.features.homefeed.application.dto.HomeFeedResponse;
import pe.com.graduate.insights.api.features.homefeed.application.ports.output.HomeFeedRepositoryPort;

@ExtendWith(MockitoExtension.class)
class HomeFeedUseCaseHandlerTest {

  @Mock private HomeFeedRepositoryPort homeFeedRepositoryPort;

  @InjectMocks private HomeFeedUseCaseHandler homeFeedUseCaseHandler;

  @Test
  void getHomeFeedShouldDelegateToRepositoryPort() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<HomeFeedResponse> expected = new PageImpl<>(List.of(), pageable, 0);
    when(homeFeedRepositoryPort.getHomeFeed(pageable)).thenReturn(expected);

    Page<HomeFeedResponse> result = homeFeedUseCaseHandler.getHomeFeed(pageable);

    assertSame(expected, result);
    verify(homeFeedRepositoryPort).getHomeFeed(pageable);
  }
}
