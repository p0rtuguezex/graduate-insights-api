package pe.com.graduate.insights.api.features.graduate.infrastructure.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.TitulationModeEntity;

public interface TitulationModeRepository extends JpaRepository<TitulationModeEntity, Long> {
  List<TitulationModeEntity> findAllByEstadoOrderByNombreAsc(String estado);
}
