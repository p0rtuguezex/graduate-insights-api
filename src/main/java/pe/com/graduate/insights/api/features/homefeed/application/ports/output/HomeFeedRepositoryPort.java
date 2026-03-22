package pe.com.graduate.insights.api.features.homefeed.application.ports.output;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.features.homefeed.application.dto.HomeFeedResponse;

public interface HomeFeedRepositoryPort {
  Page<HomeFeedResponse> getHomeFeed(Pageable pageable);

  List<HomeFeedResponse> getRecentHomeFeed(int limit);
}
