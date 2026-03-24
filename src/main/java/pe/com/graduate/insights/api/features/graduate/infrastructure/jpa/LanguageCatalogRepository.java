package pe.com.graduate.insights.api.features.graduate.infrastructure.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.LanguageCatalogEntity;

public interface LanguageCatalogRepository extends JpaRepository<LanguageCatalogEntity, Long> {
  List<LanguageCatalogEntity> findAllByEstadoOrderByNombreAsc(String estado);
}
