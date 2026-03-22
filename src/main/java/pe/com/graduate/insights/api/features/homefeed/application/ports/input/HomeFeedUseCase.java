package pe.com.graduate.insights.api.features.homefeed.application.ports.input;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.features.homefeed.application.dto.HomeFeedResponse;

public interface HomeFeedUseCase {
  Page<HomeFeedResponse> getHomeFeed(Pageable pageable);

  List<HomeFeedResponse> getRecentHomeFeed(int limit);
}
