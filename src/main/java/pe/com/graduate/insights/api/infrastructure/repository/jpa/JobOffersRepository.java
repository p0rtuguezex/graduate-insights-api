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

  Optional<JobOffersEntity> findByIdAndEstado(Long id, String estado);

  @Query("SELECT j FROM JobOffersEntity j WHERE j.id = :id AND j.estado = :estado AND j.employer.id = :employerId")
  Optional<JobOffersEntity> findByIdAndEstadoAndEmployerId(
      @Param("id") Long id, @Param("estado") String estado, @Param("employerId") Long employerId);

  @Query("SELECT j FROM JobOffersEntity j WHERE j.estado = :estado AND j.employer.user.estado = :userEstado")
  List<JobOffersEntity> findAllByEstadoAndEmployer_User_Estado(
      @Param("estado") String estado, @Param("userEstado") String userEstado);

  @Query("SELECT j FROM JobOffersEntity j WHERE j.estado = :estado AND j.employer.id = :employerId")
  List<JobOffersEntity> findAllByEstadoAndEmployerId(
      @Param("estado") String estado, @Param("employerId") Long employerId);

  @Query("SELECT j FROM JobOffersEntity j WHERE j.estado = :estado AND j.employer.id = :employerId")
  Page<JobOffersEntity> findAllByEstadoAndEmployerId(
      @Param("estado") String estado, Pageable pageable, @Param("employerId") Long employerId);

  @Query("SELECT j FROM JobOffersEntity j WHERE j.estado = :estado AND " +
         "(LOWER(j.titulo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
         "LOWER(j.descripcion) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
         "j.employer.id = :employerId")
  Page<JobOffersEntity> findAllByEstadoSearchAndEmployerId(
      @Param("search") String search, @Param("estado") String estado, 
      Pageable pageable, @Param("employerId") Long employerId);

  @Transactional
  @Modifying
  @Query("UPDATE JobOffersEntity j SET j.estado = '0' WHERE j.id = :jobOfferId")
  void deactivateJobOffers(@Param("jobOfferId") Long jobOfferId);

  // Métodos para directores - obtener todas las ofertas de trabajo
  Page<JobOffersEntity> findAllByEstado(String status, Pageable pageable);

  @Query(
      "SELECT j FROM JobOffersEntity j "
          + "WHERE j.estado = :status "
          + "AND ("
          + " LOWER(j.link) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.titulo) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.descripcion) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.employer.user.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(j.employer.user.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<JobOffersEntity> findAllByEstadoAndSearch(String search, String status, Pageable pageable);
}
