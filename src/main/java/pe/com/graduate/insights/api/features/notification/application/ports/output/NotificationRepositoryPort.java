package pe.com.graduate.insights.api.features.notification.application.ports.output;

import java.util.List;
import pe.com.graduate.insights.api.features.notification.application.dto.NotificationResponse;

public interface NotificationRepositoryPort {

  List<NotificationResponse> getByUserId(Long userId);

  long countUnread(Long userId);

  void markAsRead(Long id, Long userId);

  void markAllAsRead(Long userId);

  void delete(Long id, Long userId);
}
