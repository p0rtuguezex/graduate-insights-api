package pe.com.graduate.insights.api.features.surveytype.application.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.surveytype.application.dto.SurveyTypeRequest;
import pe.com.graduate.insights.api.features.surveytype.application.ports.output.SurveyTypeRepositoryPort;
import pe.com.graduate.insights.api.features.surveytype.domain.exception.SurveyTypeException;

@ExtendWith(MockitoExtension.class)
class SurveyTypeUseCaseHandlerTest {

  @Mock private SurveyTypeRepositoryPort surveyTypeRepositoryPort;

  @InjectMocks private SurveyTypeUseCaseHandler surveyTypeUseCaseHandler;

  @Test
  void saveShouldFailWhenNameAlreadyExists() {
    SurveyTypeRequest request = new SurveyTypeRequest();
    request.setName("Empleabilidad");
    request.setActive(Boolean.TRUE);
    when(surveyTypeRepositoryPort.existsByName("Empleabilidad")).thenReturn(true);

    assertThrows(SurveyTypeException.class, () -> surveyTypeUseCaseHandler.save(request));
  }

  @Test
  void updateShouldValidateUniqueNameExcludingCurrentId() {
    SurveyTypeRequest request = new SurveyTypeRequest();
    request.setName("Seguimiento");
    request.setActive(Boolean.TRUE);
    when(surveyTypeRepositoryPort.existsByNameAndIdNot("Seguimiento", 5L)).thenReturn(false);

    surveyTypeUseCaseHandler.update(request, 5L);

    verify(surveyTypeRepositoryPort).update(request, 5L);
  }
}
