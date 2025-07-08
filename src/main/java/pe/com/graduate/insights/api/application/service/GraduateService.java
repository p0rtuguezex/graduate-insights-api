package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.com.graduate.insights.api.application.ports.input.GraduateUseCase;
import pe.com.graduate.insights.api.application.ports.output.GraduateRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

@Service
@AllArgsConstructor
public class GraduateService implements GraduateUseCase {

  private final GraduateRepositoryPort graduateRepositoryPort;
  private final FileService fileService;

  @Override
  public void save(GraduateRequest request) {
    graduateRepositoryPort.save(request);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return graduateRepositoryPort.getList();
  }

  @Override
  public Page<GraduateResponse> getPagination(String search, Pageable pageable) {
    return graduateRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public GraduateResponse getDomain(Long id) {
    return graduateRepositoryPort.getDomain(id);
  }

  @Override
  public void update(GraduateRequest request, Long id) {
    graduateRepositoryPort.update(request, id);
  }

  @Override
  public void delete(Long id) {
    graduateRepositoryPort.delete(id);
  }

  @Override
  public void uploadCv(MultipartFile file, Long graduateId) {
    // Verificar que el graduado existe
    GraduateResponse graduate = graduateRepositoryPort.getDomain(graduateId);
    if (graduate == null) {
      throw new NotFoundException("Graduado no encontrado con ID: " + graduateId);
    }

    // Si ya existe un CV, eliminarlo primero
    if (graduate.getCvPath() != null && !graduate.getCvPath().trim().isEmpty()) {
      fileService.deleteCvFile(graduate.getCvPath());
    }

    // Guardar el nuevo archivo
    String fileName = fileService.storeCvFile(file, graduateId);

    // Actualizar la ruta del CV en la base de datos
    graduateRepositoryPort.updateCvPath(graduateId, fileName);
  }

  @Override
  public Resource downloadCv(Long graduateId) {
    // Verificar que el graduado existe
    GraduateResponse graduate = graduateRepositoryPort.getDomain(graduateId);
    if (graduate == null) {
      throw new NotFoundException("Graduado no encontrado con ID: " + graduateId);
    }

    // Verificar que tiene un CV
    if (graduate.getCvPath() == null || graduate.getCvPath().trim().isEmpty()) {
      throw new NotFoundException("El graduado no tiene un CV cargado");
    }

    // Verificar que el archivo existe
    if (!fileService.fileExists(graduate.getCvPath())) {
      throw new NotFoundException("El archivo CV no existe en el sistema");
    }

    return fileService.loadCvFileAsResource(graduate.getCvPath());
  }
}
