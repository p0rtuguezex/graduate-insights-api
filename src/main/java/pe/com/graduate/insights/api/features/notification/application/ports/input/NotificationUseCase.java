package pe.com.graduate.insights.api.features.notification.application.ports.input;

import java.util.List;
import pe.com.graduate.insights.api.features.notification.application.dto.NotificationResponse;

public interface NotificationUseCase {

  List<NotificationResponse> getMyNotifications(Long userId);

  long countUnread(Long userId);

  void markAsRead(Long id, Long userId);

  void markAllAsRead(Long userId);

  void delete(Long id, Long userId);
}
