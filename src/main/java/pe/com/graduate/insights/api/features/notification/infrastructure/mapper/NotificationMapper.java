package pe.com.graduate.insights.api.features.notification.infrastructure.mapper;

import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.notification.application.dto.NotificationResponse;
import pe.com.graduate.insights.api.features.notification.infrastructure.entity.NotificationEntity;

@Component
public class NotificationMapper {

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  public NotificationResponse toResponse(NotificationEntity entity) {
    return NotificationResponse.builder()
        .id(entity.getId())
        .userId(entity.getUserId())
        .titulo(entity.getTitulo())
        .mensaje(entity.getMensaje())
        .tipo(entity.getTipo())
        .leido(entity.isLeido())
        .fechaCreacion(
            entity.getCreatedDate() != null
                ? entity.getCreatedDate().format(FORMATTER)
                : null)
        .build();
  }
}
