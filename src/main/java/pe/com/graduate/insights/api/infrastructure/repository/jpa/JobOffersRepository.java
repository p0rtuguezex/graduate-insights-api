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
import pe.com.graduate.insights.api.infrastructure.repository.entities.JobOffersEntity;

public interface JobOffersRepository extends JpaRepository<JobOffersEntity, Long> {

  Optional<JobOffersEntity> findByIdAndEstadoAndEmployerId(
      Long jobOfferId, String estado, Long employerId);

  List<JobOffersEntity> findAllByEstadoAndEmployerId(String estado, Long employerId);

  Page<JobOffersEntity> findAllByEstadoAndEmployerId(
      String status, Pageable pageable, Long employerId);

  @Query(
      "SELECT j FROM JobOffersEntity j "
          + "WHERE j.estado = :status and j.employer.id = :employerId "
          + "AND ("
          + " LOWER(j.link) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.titulo) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.descripcion) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<JobOffersEntity> findAllByEstadoSearchAndEmployerId(
      String search, String status, Pageable pageable, Long employerId);

  @Transactional
  @Modifying
  @Query("UPDATE JobOffersEntity j SET j.estado = '0' WHERE j.id = :jobOfferId")
  void deactivateJobOffers(@Param("jobOfferId") Long jobOfferId);
}
