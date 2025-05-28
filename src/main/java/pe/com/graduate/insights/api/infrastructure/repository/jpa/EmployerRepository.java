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
import pe.com.graduate.insights.api.infrastructure.repository.entities.EmployerEntity;

public interface EmployerRepository extends JpaRepository<EmployerEntity, Long> {

  boolean existsByIdAndUserEstado(Long employerId, String estado);

  Optional<EmployerEntity> findByIdAndUserEstado(Long id, String estado);

  Page<EmployerEntity> findAllByUserEstado(String status, Pageable pageable);

  List<EmployerEntity> findAllByUserEstado(String status);

  @Query(
      "SELECT e FROM EmployerEntity e "
          + "WHERE e.user.estado = :status "
          + "AND ("
          + " LOWER(e.ruc) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(e.razonSocial) LIKE CONCAT('%', :search, '%') OR "
          + " LOWER(e.user.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(e.user.correo) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(e.user.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(e.user.genero) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(e.user.estado) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(e.user.dni) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(e.user.celular) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<EmployerEntity> findAllByUserEstadoSearch(String search, String status, Pageable pageable);

  @Transactional
  @Modifying
  @Query(
      "UPDATE UserEntity u SET u.estado = '0' WHERE u.id = (SELECT g.user.id FROM EmployerEntity g WHERE g.id = :employerId)")
  void deactivateUserByEmployerId(@Param("employerId") Long employerId);
}
