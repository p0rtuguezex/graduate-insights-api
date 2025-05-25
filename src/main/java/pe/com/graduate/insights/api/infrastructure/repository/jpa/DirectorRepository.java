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

  Optional<DirectorEntity> findByUserIdAndUserEstado(Long id, String estado); // metodo distinto

  Optional<DirectorEntity> findByIdAndUserEstado(Long id, String estado);

  List<DirectorEntity> findAllByUserEstado(String estado);

  Page<DirectorEntity> findAllByUserEstado(String status, Pageable pageable);

  @Query(
      """
          SELECT d FROM DirectorEntity d
          WHERE d.user.estado = :status
           AND (
           LOWER(d.escuela) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(d.user.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR
             LOWER(d.user.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(d.user.genero) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(d.user.correo) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(d.user.estado) LIKE LOWER(CONCAT('%', :search, '%')) OR
                 LOWER(d.user.dni) LIKE LOWER(CONCAT('%', :search, '%')) OR
                  LOWER(d.user.celular) LIKE LOWER(CONCAT('%', :search, '%')))
          """)
  Page<DirectorEntity> findAllByUserEstadoSearch(
      @Param("search") String search, @Param("status") String status, Pageable pageable);

  @Transactional
  @Modifying
  @Query(
      "UPDATE UserEntity u SET u.estado = '0' WHERE u.id = (SELECT d.user.id FROM DirectorEntity d WHERE d.id = :directorId)")
  void desactivateUserByDirectorId(@Param("directorId") Long directorId);
}
