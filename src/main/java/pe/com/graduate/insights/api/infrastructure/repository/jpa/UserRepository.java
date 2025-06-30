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
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByIdAndEstado(Long id, String status);

  Optional<UserEntity> findByCorreoAndEstado(String correo, String status);

  List<UserEntity> findAllByEstado(String status);

  Page<UserEntity> findAllByEstado(String status, Pageable pageable);

  @Query(
      "SELECT u FROM UserEntity u "
          + "WHERE "
          + "u.estado=:status "
          + "and (u.nombres LIKE %:search% OR u.correo LIKE %:search% "
          + "OR u.apellidos LIKE %:search% "
          + "OR u.genero LIKE %:search% "
          + "OR u.estado LIKE %:search% "
          + "OR u.dni LIKE %:search% "
          + "OR u.celular LIKE %:search% )")
  Page<UserEntity> findAllByEstadoSearch(String search, String status, Pageable pageable);

  @Transactional
  @Modifying
  @Query("UPDATE UserEntity e SET e.estado = :status WHERE e.id = :id")
  void updateStatusById(Long id, String status);

  @Transactional
  Optional<UserEntity> findByCorreo(String email);

  @Modifying
  @Transactional
  @Query(
      "UPDATE UserEntity u SET u.passwordRecoveryCode = :code WHERE u.id = :userId AND u.estado='1' ")
  void updateRecoveryCodeByUserId(@Param("code") String code, @Param("userId") Long userId);

  @Modifying
  @Transactional
  @Query("UPDATE UserEntity u SET u.contrasena = :newPwd WHERE u.id = :userId AND  u.estado='1' ")
  void updatePasswordByUserId(@Param("newPwd") String newPwd, @Param("userId") Long userId);

  @Modifying
  @Transactional
  @Query("UPDATE UserEntity u SET u.codeConfirm = :code WHERE u.id = :userId AND u.estado='1'")
  void updateCodeConfirmByUserId(@Param("code") String code, @Param("userId") Long userId);

  @Modifying
  @Transactional
  @Query("UPDATE UserEntity u SET u.verificado= true WHERE u.id = :userId AND u.estado='1'")
  void updateVerifiedTrueByUserId(@Param("userId") Long userId);
}
