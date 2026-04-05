package pe.com.graduate.insights.api.features.graduate.infrastructure.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.ProfessionalSchoolEntity;

public interface ProfessionalSchoolRepository
    extends JpaRepository<ProfessionalSchoolEntity, Long> {
  List<ProfessionalSchoolEntity> findAllByEstadoOrderByNombreAsc(String estado);

  List<ProfessionalSchoolEntity> findAllByFacultadIdAndEstadoOrderByNombreAsc(
      Long facultadId, String estado);
}
