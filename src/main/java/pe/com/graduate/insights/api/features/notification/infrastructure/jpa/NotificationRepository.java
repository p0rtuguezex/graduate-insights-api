package pe.com.graduate.insights.api.features.notification.infrastructure.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.com.graduate.insights.api.features.notification.infrastructure.entity.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

  List<NotificationEntity> findByUserIdOrderByCreatedDateDesc(Long userId);

  long countByUserIdAndLeidoFalse(Long userId);

  Optional<NotificationEntity> findByIdAndUserId(Long id, Long userId);

  @Transactional
  @Modifying
  @Query("UPDATE NotificationEntity n SET n.leido = true WHERE n.id = :id AND n.userId = :userId")
  void markAsRead(@Param("id") Long id, @Param("userId") Long userId);

  @Transactional
  @Modifying
  @Query("UPDATE NotificationEntity n SET n.leido = true WHERE n.userId = :userId")
  void markAllAsRead(@Param("userId") Long userId);

  @Transactional
  void deleteByIdAndUserId(Long id, Long userId);
}
