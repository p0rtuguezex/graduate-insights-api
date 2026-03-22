package pe.com.graduate.insights.api.features.graduate.application.usecase;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.features.graduate.application.ports.input.GraduateWriteUseCase;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateFileStoragePort;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateWriteRepositoryPort;

@Service
@RequiredArgsConstructor
public class GraduateWriteUseCaseHandler implements GraduateWriteUseCase {

  private final GraduateWriteRepositoryPort graduateWriteRepositoryPort;
  private final GraduateFileStoragePort graduateFileStoragePort;

  @Override
  public void save(GraduateRequest request) {
    graduateWriteRepositoryPort.save(request);
  }

  @Override
  public void update(GraduateRequest request, Long id) {
    GraduateResponse currentGraduate = graduateWriteRepositoryPort.getDomain(id);
    String currentCvPath = currentGraduate.getCvPath();
    String newCvPath = request.getCvPath();

    if (StringUtils.isNotBlank(currentCvPath)
        && (newCvPath == null || !currentCvPath.equals(newCvPath))) {
      graduateFileStoragePort.deleteFile(currentCvPath);
    }

    graduateWriteRepositoryPort.update(request, id);
  }

  @Override
  public void delete(Long id) {
    GraduateResponse graduate = graduateWriteRepositoryPort.getDomain(id);
    if (StringUtils.isNotBlank(graduate.getCvPath())) {
      graduateFileStoragePort.deleteFile(graduate.getCvPath());
    }
    graduateWriteRepositoryPort.delete(id);
  }

  @Override
  public void activate(Long id) {
    graduateWriteRepositoryPort.updateValidationStatus(id, Boolean.TRUE);
  }
}