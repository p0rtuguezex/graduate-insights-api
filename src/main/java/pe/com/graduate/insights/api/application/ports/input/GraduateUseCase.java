package pe.com.graduate.insights.api.application.ports.input;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

public interface GraduateUseCase
    extends GenericCreate<GraduateRequest>,
        GenericUpdate<GraduateRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<GraduateResponse>,
        GenericPaginate<GraduateResponse>,
        GenericDelete {

  /**
   * Sube un archivo CV para un graduado
   *
   * @param file Archivo PDF a subir
   * @param graduateId ID del graduado
   */
  void uploadCv(MultipartFile file, Long graduateId);

  /**
   * Descarga el CV de un graduado
   *
   * @param graduateId ID del graduado
   * @return Recurso del archivo CV
   */
  Resource downloadCv(Long graduateId);
}
