package pe.com.graduate.insights.api.features.surveystatistics.domain.port.output;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import pe.com.graduate.insights.api.features.survey.domain.model.QuestionType;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;

public interface SurveyStatisticsRepositoryPort {

  long countSurveys();

  long countSurveysByStatus(SurveyStatus status);

  List<Long> findRecentSurveyIds(int limit);

  long countActiveGraduates();

  List<SurveyResponseData> findSurveyResponsesBySurveyId(Long surveyId);

  List<SurveyResponseData> findCompletedSurveyResponsesBySurveyId(Long surveyId);

  List<SurveyResponseData> findAllSurveyResponses();

  List<QuestionResponseData> findQuestionResponsesBySurveyId(Long surveyId);

  List<QuestionResponseData> findQuestionResponsesBySurveyIdAndQuestionId(Long surveyId, Long questionId);

  Long countQuestionResponsesByQuestionId(Long questionId);

  List<Integer> findNumericResponsesByQuestionId(Long questionId);

  List<String> findTextResponsesByQuestionId(Long questionId);

  Map<String, Long> countResponsesByOptionForQuestion(Long questionId);

  record SurveyResponseData(LocalDateTime submittedAt, Integer graduationYear, String gender, String graduateFullName) {}

  record QuestionResponseData(
      Long questionId,
      String questionText,
      QuestionType questionType,
      boolean required,
      List<String> selectedOptionTexts,
      String textResponse,
      Integer numericResponse,
      LocalDateTime submittedAt,
      Integer graduationYear,
      String gender,
      String graduateFullName) {}
}