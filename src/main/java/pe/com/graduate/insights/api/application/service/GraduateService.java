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
    // Obtener el graduado actual para verificar el cvPath
    GraduateResponse currentGraduate = graduateRepositoryPort.getDomain(id);
    if (currentGraduate == null) {
      throw new NotFoundException("Graduado no encontrado con ID: " + id);
    }

    String currentCvPath = currentGraduate.getCvPath();
    String newCvPath = request.getCvPath();

    // Caso 1: Si el nuevo cvPath es null y había un archivo previo, eliminar el archivo
    if (newCvPath == null && currentCvPath != null && !currentCvPath.trim().isEmpty()) {
      fileService.deleteFile(currentCvPath);
    }
    // Caso 2: Si el nuevo cvPath es diferente al actual y había un archivo previo, eliminar el archivo antiguo
    else if (newCvPath != null && currentCvPath != null && !currentCvPath.equals(newCvPath)) {
      fileService.deleteFile(currentCvPath);
    }

    // Actualizar los datos del graduado
    graduateRepositoryPort.update(request, id);
  }

  @Override
  public void delete(Long id) {
    // Obtener el graduado para eliminar su CV si existe
    GraduateResponse graduate = graduateRepositoryPort.getDomain(id);
    if (graduate != null && graduate.getCvPath() != null && !graduate.getCvPath().trim().isEmpty()) {
      fileService.deleteFile(graduate.getCvPath());
    }
    
    graduateRepositoryPort.delete(id);
  }

}
