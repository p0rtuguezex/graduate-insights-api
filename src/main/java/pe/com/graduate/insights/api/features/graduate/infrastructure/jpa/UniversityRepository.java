package pe.com.graduate.insights.api.features.graduate.infrastructure.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.UniversityEntity;

public interface UniversityRepository extends JpaRepository<UniversityEntity, Long> {
  List<UniversityEntity> findAllByEstadoOrderByNombreAsc(String estado);
}
