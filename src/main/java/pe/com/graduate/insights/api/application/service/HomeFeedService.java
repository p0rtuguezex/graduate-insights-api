package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.HomeFeedUseCase;
import pe.com.graduate.insights.api.application.ports.output.HomeFeedRepositoryPort;
import pe.com.graduate.insights.api.domain.models.response.HomeFeedResponse;

@Service
@RequiredArgsConstructor
public class HomeFeedService implements HomeFeedUseCase {

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
