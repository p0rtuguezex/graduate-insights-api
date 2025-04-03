package pe.com.graduate.insights.api.infrastructure.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.graduate.insights.api.infrastructure.adapter.entities.GraduateEntity;

@Repository
public interface GraduateRepository extends JpaRepository<GraduateEntity, Long> {}
