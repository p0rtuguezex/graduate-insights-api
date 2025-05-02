package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {}
