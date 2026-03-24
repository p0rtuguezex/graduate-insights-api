package pe.com.graduate.insights.api.features.graduate.infrastructure.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.DegreeTypeEntity;

public interface DegreeTypeRepository extends JpaRepository<DegreeTypeEntity, Long> {
  List<DegreeTypeEntity> findAllByEstadoOrderByNombreAsc(String estado);
}
