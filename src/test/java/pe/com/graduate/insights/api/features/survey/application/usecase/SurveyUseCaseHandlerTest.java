package pe.com.graduate.insights.api.features.survey.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;
import pe.com.graduate.insights.api.features.survey.application.ports.output.SurveyRepositoryPort;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;

@ExtendWith(MockitoExtension.class)
class SurveyUseCaseHandlerTest {

  @Mock private SurveyRepositoryPort surveyRepositoryPort;

  @InjectMocks private SurveyUseCaseHandler surveyUseCaseHandler;

  @Test
  void getActiveSurveysShouldDelegateToRepositoryPort() {
    List<SurveyResponse> expected = List.of();
    when(surveyRepositoryPort.getActiveSurveys()).thenReturn(expected);

    List<SurveyResponse> result = surveyUseCaseHandler.getActiveSurveys();

    assertSame(expected, result);
    verify(surveyRepositoryPort).getActiveSurveys();
  }

  @Test
  void updateStatusShouldNormalizeAndDelegateToRepositoryPort() {
    surveyUseCaseHandler.updateStatus(5L, "active");

    verify(surveyRepositoryPort).updateStatus(5L, SurveyStatus.ACTIVE);
  }

  @Test
  void getSurveysByStatusShouldNormalizeAndDelegateToRepositoryPort() {
    List<SurveyResponse> expected = List.of();
    when(surveyRepositoryPort.getSurveysByStatus(SurveyStatus.COMPLETED)).thenReturn(expected);

    List<SurveyResponse> result = surveyUseCaseHandler.getSurveysByStatus("completed");

    assertSame(expected, result);
    verify(surveyRepositoryPort).getSurveysByStatus(SurveyStatus.COMPLETED);
  }
}


