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
import pe.com.graduate.insights.api.infrastructure.repository.entities.EventTypesEntity;

public interface EventTypesRepository extends JpaRepository<EventTypesEntity, Long> {

  Optional<EventTypesEntity> findByIdAndEstado(Long id, String estado);

  Optional<EventTypesEntity> findByNombreAndEstado(String name, String estado);

  Page<EventTypesEntity> findAllByEstado(String status, Pageable pageable);

  List<EventTypesEntity> findAllByEstado(String status);

  @Query(
      "SELECT e FROM EventTypesEntity e "
          + "WHERE e.estado = :status "
          + "AND ("
          + " LOWER(e.nombre) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<EventTypesEntity> findAllByEstadoSearch(String search, String status, Pageable pageable);

  @Transactional
  @Modifying
  @Query("UPDATE EventTypesEntity e SET e.estado = '0' WHERE e.id = :eventTypeId")
  void deactivateEventType(@Param("eventTypeId") Long eventTypeId);
}
