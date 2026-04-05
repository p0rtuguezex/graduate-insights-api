package pe.com.graduate.insights.api.features.educationcenter.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.educationcenter.application.dto.EducationCenterResponse;
import pe.com.graduate.insights.api.features.educationcenter.application.ports.output.EducationCenterRepositoryPort;

@ExtendWith(MockitoExtension.class)
class EducationCenterUseCaseHandlerTest {

  @Mock private EducationCenterRepositoryPort educationCenterRepositoryPort;

  @InjectMocks private EducationCenterUseCaseHandler educationCenterUseCaseHandler;

  @Test
  void getListShouldDelegateToRepositoryPort() {
    List<EducationCenterResponse> expected = List.of();
    when(educationCenterRepositoryPort.getList()).thenReturn(expected);

    List<EducationCenterResponse> result = educationCenterUseCaseHandler.getList();

    assertSame(expected, result);
    verify(educationCenterRepositoryPort).getList();
  }

  @Test
  void getDomainShouldDelegateToRepositoryPort() {
    EducationCenterResponse expected = EducationCenterResponse.builder().build();
    when(educationCenterRepositoryPort.getDomain(4L)).thenReturn(expected);

    EducationCenterResponse result = educationCenterUseCaseHandler.getDomain(4L);

    assertSame(expected, result);
    verify(educationCenterRepositoryPort).getDomain(4L);
  }
}
