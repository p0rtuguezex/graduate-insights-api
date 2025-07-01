package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyStatus;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {

  Optional<SurveyEntity> findByTitle(String title);

  List<SurveyEntity> findBySurveyTypeId(Long surveyTypeId);

  Page<SurveyEntity> findBySurveyTypeId(Long surveyTypeId, Pageable pageable);

  Page<SurveyEntity> findByTitleContainingIgnoreCase(String search, Pageable pageable);

  Page<SurveyEntity> findBySurveyTypeIdAndTitleContainingIgnoreCase(
      Long surveyTypeId, String search, Pageable pageable);

  // Métodos para estado de encuesta
  List<SurveyEntity> findByStatus(SurveyStatus status);

  Page<SurveyEntity> findByStatus(SurveyStatus status, Pageable pageable);

  Page<SurveyEntity> findByStatusAndTitleContainingIgnoreCase(
      SurveyStatus status, String search, Pageable pageable);

  // Métodos para fechas
  @Query(
      "SELECT s FROM SurveyEntity s WHERE s.startDate <= :now AND (s.endDate IS NULL OR s.endDate >= :now) AND s.status = :status")
  List<SurveyEntity> findActiveInPeriod(
      @Param("now") LocalDateTime now, @Param("status") SurveyStatus status);

  @Query("SELECT s FROM SurveyEntity s WHERE s.endDate < :now AND s.status = :status")
  List<SurveyEntity> findExpiredSurveys(
      @Param("now") LocalDateTime now, @Param("status") SurveyStatus status);

  List<SurveyEntity> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

  List<SurveyEntity> findByEndDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
