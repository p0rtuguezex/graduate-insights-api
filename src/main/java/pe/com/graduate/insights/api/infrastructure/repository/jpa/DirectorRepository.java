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
import pe.com.graduate.insights.api.infrastructure.repository.entities.DirectorEntity;

@Repository
public interface DirectorRepository extends JpaRepository<DirectorEntity, Long> {

  boolean existsByIdAndUserEstado(Long graduateId, String estado);
  
  boolean existsByUserIdAndUserEstado(Long userId, String estado);

  Optional<DirectorEntity> findByIdAndUserEstado(Long id, String estado);

  Page<DirectorEntity> findAllByUserEstado(String status, Pageable pageable);

  List<DirectorEntity> findAllByUserEstado(String status);

  @Query(
      "SELECT g FROM DirectorEntity g "
          + "WHERE g.user.estado = :status "
          + "AND ("
          + " LOWER(g.user.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.correo) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.genero) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.estado) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.dni) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + " LOWER(g.user.celular) LIKE LOWER(CONCAT('%', :search, '%')) "
          + ")")
  Page<DirectorEntity> findAllByUserEstadoSearch(String search, String status, Pageable pageable);

  @Transactional
  @Modifying
  @Query(
      "UPDATE UserEntity u SET u.estado = '0' WHERE u.id = (SELECT g.user.id FROM DirectorEntity g WHERE g.id = :directorId)")
  void deactivateUserByDirectorId(@Param("directorId") Long directorId);
}
