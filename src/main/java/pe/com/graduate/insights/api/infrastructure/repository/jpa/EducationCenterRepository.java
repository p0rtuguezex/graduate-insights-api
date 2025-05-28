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
import pe.com.graduate.insights.api.infrastructure.repository.entities.EducationCenterEntity;

public interface EducationCenterRepository extends JpaRepository<EducationCenterEntity, Long> {

  Optional<EducationCenterEntity> findByIdAndEstado(Long id, String estado);

  Optional<EducationCenterEntity> findByNombreAndEstado(String name, String estado);

  Page<EducationCenterEntity> findAllByEstado(String status, Pageable pageable);

  List<EducationCenterEntity> findAllByEstado(String status);

  @Query(
      "SELECT e FROM EducationCenterEntity e "
          + "WHERE e.estado = :status "
          + "AND ("
          + " LOWER(e.direccion) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<EducationCenterEntity> findAllByEstadoSearch(
      String search, String status, Pageable pageable);

  @Transactional
  @Modifying
  @Query("UPDATE EducationCenterEntity e SET e.estado = '0' WHERE e.id = :educationCenterId")
  void deactivateEducationCenter(@Param("educationCenterId") Long educationCenterId);
}
