package pe.com.graduate.insights.api.infrastructure.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateQuestionResponseEntity;

@Repository
public interface GraduateQuestionResponseRepository
    extends JpaRepository<GraduateQuestionResponseEntity, Long> {

  /** Obtiene todas las respuestas para una pregunta específica */
  List<GraduateQuestionResponseEntity> findByQuestionId(Long questionId);

  /** Obtiene todas las respuestas de una encuesta específica */
  @Query(
      "SELECT qr FROM GraduateQuestionResponseEntity qr WHERE qr.graduateSurveyResponse.survey.id = :surveyId")
  List<GraduateQuestionResponseEntity> findBySurveyId(@Param("surveyId") Long surveyId);

  /** Obtiene todas las respuestas de una encuesta y pregunta específica */
  @Query(
      "SELECT qr FROM GraduateQuestionResponseEntity qr WHERE qr.graduateSurveyResponse.survey.id = :surveyId AND qr.question.id = :questionId")
  List<GraduateQuestionResponseEntity> findBySurveyIdAndQuestionId(
      @Param("surveyId") Long surveyId, @Param("questionId") Long questionId);

  /** Cuenta las respuestas por opción para una pregunta específica */
  @Query(
      "SELECT qo.optionText, COUNT(qr) FROM GraduateQuestionResponseEntity qr "
          + "JOIN qr.selectedOptions qo "
          + "WHERE qr.question.id = :questionId AND qo.question.id = :questionId "
          + "GROUP BY qo.id, qo.optionText")
  List<Object[]> countResponsesByOptionForQuestion(@Param("questionId") Long questionId);

  /** Obtiene todas las respuestas numéricas para una pregunta */
  @Query(
      value =
          "SELECT numeric_response FROM graduate_question_responses "
              + "WHERE question_id = :questionId AND numeric_response IS NOT NULL",
      nativeQuery = true)
  List<Integer> findNumericResponsesByQuestionId(@Param("questionId") Long questionId);

  /** Obtiene todas las respuestas de texto para una pregunta */
  @Query(
      "SELECT qr.textResponse FROM GraduateQuestionResponseEntity qr "
          + "WHERE qr.question.id = :questionId AND qr.textResponse IS NOT NULL")
  List<String> findTextResponsesByQuestionId(@Param("questionId") Long questionId);

  /** Cuenta el total de respuestas para una pregunta */
  @Query(
      "SELECT COUNT(qr) FROM GraduateQuestionResponseEntity qr WHERE qr.question.id = :questionId")
  Long countResponsesByQuestionId(@Param("questionId") Long questionId);

  /** Cuenta el total de respuestas para una encuesta */
  @Query(
      "SELECT COUNT(DISTINCT qr.graduateSurveyResponse.id) FROM GraduateQuestionResponseEntity qr "
          + "WHERE qr.graduateSurveyResponse.survey.id = :surveyId")
  Long countDistinctResponsesBySurveyId(@Param("surveyId") Long surveyId);

  /** Método de debug para verificar respuestas numéricas específicas */
  @Query(
      value =
          "SELECT id, question_id, numeric_response FROM graduate_question_responses "
              + "WHERE question_id = :questionId",
      nativeQuery = true)
  List<Object[]> debugNumericResponsesByQuestionId(@Param("questionId") Long questionId);
}
