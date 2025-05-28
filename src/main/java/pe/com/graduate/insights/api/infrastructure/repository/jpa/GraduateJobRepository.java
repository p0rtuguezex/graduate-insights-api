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
public interface GraduateJobRepository extends JpaRepository<JobEntity, Long> {

  Optional<JobEntity> findByIdAndEstadoAndGraduateId(Long jobId, String estado, Long graduateId);

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
          + " LOWER(j.modalidad) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<JobEntity> findAllByEstadoSearchAndGraduateId(
      String search, String status, Pageable pageable, Long graduateId);

  @Transactional
  @Modifying
  @Query("UPDATE JobEntity j SET j.estado = '0' WHERE j.id = :jobId")
  void deactivateJob(@Param("jobId") Long jobId);
}
