package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyType;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {

  Optional<SurveyEntity> findByTitle(String title);

  List<SurveyEntity> findBySurveyType(SurveyType surveyType);

  Page<SurveyEntity> findBySurveyType(SurveyType surveyType, Pageable pageable);

  Page<SurveyEntity> findByTitleContainingIgnoreCase(String search, Pageable pageable);

  Page<SurveyEntity> findBySurveyTypeAndTitleContainingIgnoreCase(
      SurveyType surveyType, String search, Pageable pageable);
}
