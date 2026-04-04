package pe.com.graduate.insights.api.features.notification.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.features.auth.application.ports.input.CurrentUserUseCase;
import pe.com.graduate.insights.api.features.notification.application.dto.NotificationResponse;
import pe.com.graduate.insights.api.features.notification.application.ports.input.NotificationUseCase;
import pe.com.graduate.insights.api.shared.models.response.ApiResponse;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "APIs para gestión de notificaciones del usuario")
public class NotificationController {

  private final NotificationUseCase notificationUseCase;
  private final CurrentUserUseCase currentUserUseCase;

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return currentUserUseCase.getCurrentUser(authentication).id();
  }

  @Operation(summary = "Obtener mis notificaciones")
  @GetMapping
  public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications() {
    Long userId = getCurrentUserId();
    List<NotificationResponse> notifications = notificationUseCase.getMyNotifications(userId);
    return ResponseUtils.successResponse(notifications);
  }

  @Operation(summary = "Contar notificaciones no leídas")
  @GetMapping("/unread-count")
  public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
    Long userId = getCurrentUserId();
    long count = notificationUseCase.countUnread(userId);
    return ResponseUtils.successResponse(count);
  }

  @Operation(summary = "Marcar notificación como leída")
  @PatchMapping("/{id}/mark-read")
  public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
    Long userId = getCurrentUserId();
    notificationUseCase.markAsRead(id, userId);
    return ResponseUtils.successResponse();
  }

  @Operation(summary = "Marcar todas las notificaciones como leídas")
  @PatchMapping("/mark-all-read")
  public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
    Long userId = getCurrentUserId();
    notificationUseCase.markAllAsRead(userId);
    return ResponseUtils.successResponse();
  }

  @Operation(summary = "Eliminar notificación")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long id) {
    Long userId = getCurrentUserId();
    notificationUseCase.delete(id, userId);
    return ResponseUtils.successDeleteResponse();
  }
}
