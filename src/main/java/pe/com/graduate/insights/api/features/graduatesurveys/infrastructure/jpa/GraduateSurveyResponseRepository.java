package pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.entity.GraduateSurveyResponseEntity;

public interface GraduateSurveyResponseRepository
    extends JpaRepository<GraduateSurveyResponseEntity, Long> {
  List<GraduateSurveyResponseEntity> findBySurveyId(Long surveyId);

  List<GraduateSurveyResponseEntity> findByGraduateId(Long graduateId);

  Optional<GraduateSurveyResponseEntity> findBySurveyIdAndGraduateId(
      Long surveyId, Long graduateId);

  List<GraduateSurveyResponseEntity> findBySurveyIdAndCompleted(Long surveyId, boolean completed);

  boolean existsBySurveyIdAndGraduateIdAndCompleted(
      Long surveyId, Long graduateId, boolean completed);

  Optional<GraduateSurveyResponseEntity> findBySurveyIdAndGraduateIdAndCompleted(
      Long surveyId, Long graduateId, boolean completed);
}
