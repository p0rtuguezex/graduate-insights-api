package pe.com.graduate.insights.api.features.graduate.application.usecase;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateFileStoragePort;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateWriteRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GraduateWriteUseCaseHandlerTest {

  private static final String SAME_CV_PATH = "same-cv.pdf";

  @Mock private GraduateWriteRepositoryPort graduateWriteRepositoryPort;
  @Mock private GraduateFileStoragePort graduateFileStoragePort;

  @InjectMocks private GraduateWriteUseCaseHandler graduateWriteUseCaseHandler;

  @Test
  void saveShouldDelegateToWriteRepositoryPort() {
    GraduateRequest request = GraduateRequest.builder().cvPath("create-cv.pdf").build();

    graduateWriteUseCaseHandler.save(request);

    verify(graduateWriteRepositoryPort).save(request);
  }

  @Test
  void updateShouldDeletePreviousCvWhenCvPathChanges() {
    GraduateRequest request = GraduateRequest.builder().cvPath("new-cv.pdf").build();

    GraduateResponse current = GraduateResponse.builder().cvPath("old-cv.pdf").build();
    when(graduateWriteRepositoryPort.getDomain(12L)).thenReturn(current);

    graduateWriteUseCaseHandler.update(request, 12L);

    verify(graduateFileStoragePort).deleteFile("old-cv.pdf");
    verify(graduateWriteRepositoryPort).update(request, 12L);
  }

  @Test
  void updateShouldNotDeleteCvWhenCvPathDoesNotChange() {
    GraduateRequest request = GraduateRequest.builder().cvPath(SAME_CV_PATH).build();

    GraduateResponse current = GraduateResponse.builder().cvPath(SAME_CV_PATH).build();
    when(graduateWriteRepositoryPort.getDomain(13L)).thenReturn(current);

    graduateWriteUseCaseHandler.update(request, 13L);

    verify(graduateFileStoragePort, never()).deleteFile(SAME_CV_PATH);
    verify(graduateWriteRepositoryPort).update(request, 13L);
  }

  @Test
  void deleteShouldDeleteCvThenGraduate() {
    GraduateResponse current = GraduateResponse.builder().cvPath("existing-cv.pdf").build();
    when(graduateWriteRepositoryPort.getDomain(14L)).thenReturn(current);

    graduateWriteUseCaseHandler.delete(14L);

    verify(graduateFileStoragePort).deleteFile("existing-cv.pdf");
    verify(graduateWriteRepositoryPort).delete(14L);
  }

  @Test
  void activateShouldSetValidationStatusToTrue() {
    graduateWriteUseCaseHandler.activate(15L);

    verify(graduateWriteRepositoryPort).updateValidationStatus(15L, Boolean.TRUE);
  }
}
