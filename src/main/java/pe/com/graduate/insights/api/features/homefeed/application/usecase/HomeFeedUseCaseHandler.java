package pe.com.graduate.insights.api.features.homefeed.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.homefeed.application.dto.HomeFeedResponse;
import pe.com.graduate.insights.api.features.homefeed.application.ports.input.HomeFeedUseCase;
import pe.com.graduate.insights.api.features.homefeed.application.ports.output.HomeFeedRepositoryPort;

@Service
@RequiredArgsConstructor
public class HomeFeedUseCaseHandler implements HomeFeedUseCase {

  private final HomeFeedRepositoryPort homeFeedRepositoryPort;

  @Override
  public Page<HomeFeedResponse> getHomeFeed(Pageable pageable) {
    return homeFeedRepositoryPort.getHomeFeed(pageable);
  }

  @Override
  public List<HomeFeedResponse> getRecentHomeFeed(int limit) {
    return homeFeedRepositoryPort.getRecentHomeFeed(limit);
  }
}
