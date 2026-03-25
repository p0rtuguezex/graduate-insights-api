package pe.com.graduate.insights.api.features.graduate.infrastructure.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;

@Repository
public interface GraduateRepository extends JpaRepository<GraduateEntity, Long> {

  boolean existsByIdAndUserEstado(Long graduateId, String estado);

  boolean existsByUserIdAndUserEstado(Long userId, String estado);

  Optional<GraduateEntity> findByUserIdAndUserEstado(Long userId, String estado);

  @EntityGraph(attributePaths = {
      "user",
      "grados", "grados.titulation",
      "idiomas",
      "formacionesComplementarias",
      "trayectoriasLaborales"
  })
  Optional<GraduateEntity> findByIdAndUserEstado(Long id, String estado);

  Page<GraduateEntity> findAllByUserEstado(String status, Pageable pageable);

  Page<GraduateEntity> findAllByUserEstadoAndValidated(
      String status, Boolean validated, Pageable pageable);

  List<GraduateEntity> findAllByUserEstado(String status);

  @Query(
      "SELECT g FROM GraduateEntity g "
          + "WHERE g.user.estado = :status "
          + "AND g.validated = :validated "
          + "AND ("
          + " LOWER(g.user.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.correo) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.genero) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.dni) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.celular) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.codigoUniversitario) LIKE LOWER(CONCAT('%', :search, '%'))"
          + ")")
    Page<GraduateEntity> findAllByUserEstadoSearch(String search, String status, Pageable pageable);

    @Query(
      "SELECT g FROM GraduateEntity g "
        + "WHERE g.user.estado = :status "
        + "AND g.validated = :validated "
        + "AND ("
        + " LOWER(g.user.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR "
        + " LOWER(g.user.correo) LIKE LOWER(CONCAT('%', :search, '%')) OR "
        + " LOWER(g.user.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR "
        + " LOWER(g.user.genero) LIKE LOWER(CONCAT('%', :search, '%')) OR "
        + " LOWER(g.user.dni) LIKE LOWER(CONCAT('%', :search, '%')) OR "
        + " LOWER(g.user.celular) LIKE LOWER(CONCAT('%', :search, '%')) OR "
        + " LOWER(g.codigoUniversitario) LIKE LOWER(CONCAT('%', :search, '%'))"
        + ")")
    Page<GraduateEntity> findAllByUserEstadoAndValidatedSearch(
      String search, String status, Boolean validated, Pageable pageable);

  @Transactional
  @Modifying
  @Query(
      "UPDATE UserEntity u SET u.estado = '0' WHERE u.id = (SELECT g.user.id FROM GraduateEntity g WHERE g.id = :graduateId)")
  void deactivateUserByGraduateId(@Param("graduateId") Long graduateId);

  @Query("SELECT COUNT(g) FROM GraduateEntity g WHERE g.user.estado = :estado")
  Long countByUserEstado(@Param("estado") String estado);
}



