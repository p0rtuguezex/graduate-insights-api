package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyTypeEntity;

public interface SurveyTypeRepository extends JpaRepository<SurveyTypeEntity, Long> {

  @Query(
      "SELECT st FROM SurveyTypeEntity st WHERE "
          + "LOWER(st.name) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + "LOWER(st.description) LIKE LOWER(CONCAT('%', :search, '%'))")
  Page<SurveyTypeEntity> findBySearchTerm(@Param("search") String search, Pageable pageable);

  List<SurveyTypeEntity> findByActiveTrue();

  Optional<SurveyTypeEntity> findByNameIgnoreCase(String name);

  boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

  boolean existsByNameIgnoreCase(String name);
}
