package pe.com.graduate.insights.api.features.notification.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.notification.application.dto.NotificationResponse;
import pe.com.graduate.insights.api.features.notification.application.ports.input.NotificationUseCase;
import pe.com.graduate.insights.api.features.notification.application.ports.output.NotificationRepositoryPort;

@Service
@RequiredArgsConstructor
public class NotificationUseCaseHandler implements NotificationUseCase {

  private final NotificationRepositoryPort notificationRepositoryPort;

  @Override
  public List<NotificationResponse> getMyNotifications(Long userId) {
    return notificationRepositoryPort.getByUserId(userId);
  }

  @Override
  public long countUnread(Long userId) {
    return notificationRepositoryPort.countUnread(userId);
  }

  @Override
  public void markAsRead(Long id, Long userId) {
    notificationRepositoryPort.markAsRead(id, userId);
  }

  @Override
  public void markAllAsRead(Long userId) {
    notificationRepositoryPort.markAllAsRead(userId);
  }

  @Override
  public void delete(Long id, Long userId) {
    notificationRepositoryPort.delete(id, userId);
  }
}
