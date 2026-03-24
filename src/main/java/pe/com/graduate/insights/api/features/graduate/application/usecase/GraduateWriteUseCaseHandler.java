package pe.com.graduate.insights.api.features.graduate.application.usecase;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateAcademicDegreeRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateLanguageRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.features.graduate.domain.exception.GraduateException;
import pe.com.graduate.insights.api.features.graduate.application.ports.input.GraduateWriteUseCase;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateFileStoragePort;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateWriteRepositoryPort;

@Service
@RequiredArgsConstructor
public class GraduateWriteUseCaseHandler implements GraduateWriteUseCase {

  private final GraduateWriteRepositoryPort graduateWriteRepositoryPort;
  private final GraduateFileStoragePort graduateFileStoragePort;

  @Override
  public Long save(GraduateRequest request) {
    validateAcademicCollections(request);
    return graduateWriteRepositoryPort.save(request);
  }

  @Override
  public void update(GraduateRequest request, Long id) {
    validateAcademicCollections(request);

    GraduateResponse currentGraduate = graduateWriteRepositoryPort.getDomain(id);
    String currentCvPath = currentGraduate.getCvPath();
    String newCvPath = request.getCvPath();

    if (StringUtils.isNotBlank(currentCvPath)
        && StringUtils.isNotBlank(newCvPath)
        && !currentCvPath.equals(newCvPath)) {
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

  private void validateAcademicCollections(GraduateRequest request) {
    validateSchoolRelation(request);
    validateDegrees(request);
    validateLanguages(request);
  }

  private void validateSchoolRelation(GraduateRequest request) {
    boolean hasSchoolData =
        StringUtils.isNotBlank(request.getEscuelaProfesional())
            || StringUtils.isNotBlank(request.getFacultad());
    if (request.getEscuelaProfesionalId() == null && hasSchoolData) {
      throw new GraduateException("Debe seleccionar una escuela profesional valida");
    }
  }

  private void validateDegrees(GraduateRequest request) {
    if (request.getGrados() == null) {
      return;
    }

    int degreeIndex = 0;
    for (GraduateAcademicDegreeRequest degree : request.getGrados()) {
      degreeIndex++;
      if (degree != null && hasDegreeData(degree)) {
        validateDegreeTitulation(degree, degreeIndex);
      }
    }
  }

  private boolean hasDegreeData(GraduateAcademicDegreeRequest degree) {
    return degree.getTipoGradoId() != null
        || degree.getUniversidadId() != null
        || degree.getFechaGrado() != null
        || StringUtils.isNotBlank(degree.getOtroGradoNombre())
        || degree.getTitulacion() != null;
  }

  private void validateDegreeTitulation(GraduateAcademicDegreeRequest degree, int degreeIndex) {
    if (degree.getTitulacion() != null
        && degree.getTitulacion().getModalidadTitulacionId() == null
        && StringUtils.isBlank(degree.getTitulacion().getModalidadOtro())) {
      throw new GraduateException(
          String.format("La modalidad de titulacion del grado %d es obligatoria", degreeIndex));
    }
  }

  private void validateLanguages(GraduateRequest request) {
    if (request.getIdiomas() == null) {
      return;
    }

    int languageIndex = 0;
    for (GraduateLanguageRequest language : request.getIdiomas()) {
      languageIndex++;
      if (language != null && hasLanguageData(language)) {
        validateLanguageLevel(language, languageIndex);
        validateLanguageDates(language, languageIndex);
      }
    }
  }

  private boolean hasLanguageData(GraduateLanguageRequest language) {
    return language.getIdiomaId() != null
        || language.getFechaInicio() != null
        || language.getFechaFin() != null
        || StringUtils.isNotBlank(language.getAprendizaje())
        || StringUtils.isNotBlank(language.getNivel());
  }

  private void validateLanguageLevel(GraduateLanguageRequest language, int languageIndex) {
    if (StringUtils.isBlank(language.getNivel())) {
      throw new GraduateException(String.format("El nivel del idioma %d es obligatorio", languageIndex));
    }
  }

  private void validateLanguageDates(GraduateLanguageRequest language, int languageIndex) {
    if (language.getFechaInicio() != null
        && language.getFechaFin() != null
        && language.getFechaFin().isBefore(language.getFechaInicio())) {
      throw new GraduateException(
          String.format(
              "La fecha fin del idioma %d no puede ser anterior a la fecha de inicio",
              languageIndex));
    }
  }
}