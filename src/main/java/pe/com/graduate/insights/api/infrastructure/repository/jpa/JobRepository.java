package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.com.graduate.insights.api.infrastructure.repository.entities.JobEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {

  Optional<JobEntity> findByIdAndEstado(Long id, String estado);

  Optional<JobEntity> findByIdAndEstadoAndGraduateId(
      Long jobOfferId, String estado, Long graduateId);

  List<JobEntity> findAllByEstadoAndGraduate_User_Estado(String jobEstado, String userEstado);

  List<JobEntity> findAllByEstadoAndGraduateId(String estado, Long graduateId);

  Page<JobEntity> findAllByEstadoAndGraduateId(String status, Pageable pageable, Long graduateId);

  @Query(
      "SELECT j FROM JobEntity j "
          + "WHERE j.estado = :status and j.graduate.id = :graduateId "
          + "AND ("
          + " LOWER(j.compania) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " STR(j.fechaInicio) LIKE CONCAT('%', :search, '%') OR "
          + " STR(j.fechaFin) LIKE CONCAT('%', :search, '%') OR "
          + " LOWER(j.cargo) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.modalidad) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.graduate.user.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.graduate.user.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<JobEntity> findAllByEstadoAndSearchAndGraduateId(
      String search, String status, Pageable pageable, Long graduateId);

  @Transactional
  @Modifying
  @Query("UPDATE JobEntity j SET j.estado = '0' WHERE j.id = :jobId")
  void deactivateJob(@Param("jobId") Long jobId);

  // Métodos para directores - obtener todos los trabajos
  Page<JobEntity> findAllByEstado(String status, Pageable pageable);

  @Query(
      "SELECT j FROM JobEntity j "
          + "WHERE j.estado = :status "
          + "AND ("
          + " LOWER(j.compania) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " STR(j.fechaInicio) LIKE CONCAT('%', :search, '%') OR "
          + " STR(j.fechaFin) LIKE CONCAT('%', :search, '%') OR "
          + " LOWER(j.cargo) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.modalidad) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.graduate.user.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.graduate.user.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<JobEntity> findAllByEstadoAndSearch(String search, String status, Pageable pageable);
}
