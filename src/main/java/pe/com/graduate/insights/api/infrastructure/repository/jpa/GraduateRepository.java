package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;

@Repository
public interface GraduateRepository extends JpaRepository<GraduateEntity, Long> {

  @Query("SELECT g.user FROM GraduateEntity g WHERE g.id = :graduateId ")
  Optional<UserEntity> findUserByGraduateId(@Param("graduateId") Long graduateId);

  List<GraduateEntity> findAllByfechaFin(LocalDate fechaFin, Pageable pageable);

  List<GraduateEntity> findAllByfechaFin(LocalDate fechaFin);

  @Query(
      "SELECT g FROM GraduateEntity g "
          + "WHERE "
          + "g.fechaInicio=:fechaInicio "
          + "and (g.cv LIKE %:search% )")
  Page<UserEntity> findaAllByFechaInicioAndCv(
      String search, LocalDate fechaInicio, Pageable pageable);
}
