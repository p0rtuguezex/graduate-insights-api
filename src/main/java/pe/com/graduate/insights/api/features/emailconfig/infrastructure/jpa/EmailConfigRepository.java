package pe.com.graduate.insights.api.features.emailconfig.infrastructure.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.graduate.insights.api.features.emailconfig.infrastructure.entity.EmailConfigEntity;

@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfigEntity, Long> {
  Optional<EmailConfigEntity> findByActivoTrue();
}
