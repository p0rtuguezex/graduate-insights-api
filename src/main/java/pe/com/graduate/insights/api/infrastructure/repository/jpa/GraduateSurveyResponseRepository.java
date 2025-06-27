package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateSurveyResponseEntity;

public interface GraduateSurveyResponseRepository
    extends JpaRepository<GraduateSurveyResponseEntity, Long> {
  List<GraduateSurveyResponseEntity> findBySurveyId(Long surveyId);

  List<GraduateSurveyResponseEntity> findByGraduateId(Long graduateId);

  Optional<GraduateSurveyResponseEntity> findBySurveyIdAndGraduateId(
      Long surveyId, Long graduateId);

  List<GraduateSurveyResponseEntity> findBySurveyIdAndCompleted(Long surveyId, boolean completed);
}
