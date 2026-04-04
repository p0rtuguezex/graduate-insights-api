package pe.com.graduate.insights.api.features.notification.infrastructure.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.notification.application.dto.NotificationResponse;
import pe.com.graduate.insights.api.features.notification.application.ports.output.NotificationRepositoryPort;
import pe.com.graduate.insights.api.features.notification.infrastructure.jpa.NotificationRepository;
import pe.com.graduate.insights.api.features.notification.infrastructure.mapper.NotificationMapper;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  public List<NotificationResponse> getByUserId(Long userId) {
    return notificationRepository
        .findByUserIdOrderByCreatedDateDesc(userId)
        .stream()
        .map(notificationMapper::toResponse)
        .toList();
  }

  @Override
  public long countUnread(Long userId) {
    return notificationRepository.countByUserIdAndLeidoFalse(userId);
  }

  @Override
  public void markAsRead(Long id, Long userId) {
    notificationRepository.markAsRead(id, userId);
  }

  @Override
  public void markAllAsRead(Long userId) {
    notificationRepository.markAllAsRead(userId);
  }

  @Override
  public void delete(Long id, Long userId) {
    notificationRepository.deleteByIdAndUserId(id, userId);
  }
}
