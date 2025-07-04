package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

  Optional<EventEntity> findByIdAndEstado(Long id, String estado);

  Optional<EventEntity> findByNombreAndEstado(String nombre, String estado);

  Page<EventEntity> findAllByEstado(String status, Pageable pageable);

  List<EventEntity> findAllByEstado(String status);

  @Query(
      "SELECT e FROM EventEntity e "
          + "WHERE e.estado = :status "
          + "AND ("
          + " LOWER(e.nombre) LIKE LOWER(CONCAT('%', :search, '%')) "
          + " OR LOWER(e.contenido) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<EventEntity> findAllByEstadoSearch(String search, String status, Pageable pageable);

  @Transactional
  @Modifying
  @Query("UPDATE EventEntity e SET e.estado = '0' WHERE e.id = :eventId")
  void deactivateEvent(@Param("eventId") Long eventId);
}
