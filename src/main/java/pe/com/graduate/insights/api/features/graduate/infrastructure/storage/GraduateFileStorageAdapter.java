package pe.com.graduate.insights.api.features.graduate.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateFileStoragePort;
import pe.com.graduate.insights.api.shared.infrastructure.storage.FileService;

@Component
@RequiredArgsConstructor
public class GraduateFileStorageAdapter implements GraduateFileStoragePort {

  private final FileService fileService;

  @Override
  public void deleteFile(String fileName) {
    fileService.deleteFile(fileName);
  }
}
