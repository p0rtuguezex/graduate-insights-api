package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateEntity;

@Repository
public interface GraduateRepository extends JpaRepository<GraduateEntity, Long> {

  Optional<GraduateEntity> findByIdAndUserEstado(Long id, String estado);

  @Transactional
  @Modifying
  @Query(
      "UPDATE UserEntity u SET u.estado = '0' WHERE u.id = (SELECT g.user.id FROM GraduateEntity g WHERE g.id = :graduateId)")
  void deactivateUserByGraduateId(@Param("graduateId") Long graduateId);
}
