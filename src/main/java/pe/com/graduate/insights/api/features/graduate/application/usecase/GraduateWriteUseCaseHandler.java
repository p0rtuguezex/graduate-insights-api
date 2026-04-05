package pe.com.graduate.insights.api.features.graduate.application.usecase;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateAcademicDegreeRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateComplementaryTrainingRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateLanguageRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateWorkTrajectoryRequest;
import pe.com.graduate.insights.api.features.graduate.application.ports.input.GraduateWriteUseCase;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateFileStoragePort;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateWriteRepositoryPort;
import pe.com.graduate.insights.api.features.graduate.domain.exception.GraduateException;

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
    validateDegrees(request);
    validateLanguages(request);
    validateComplementaryTrainings(request);
    validateWorkTrajectories(request);
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
      throw new GraduateException(
          String.format("El nivel del idioma %d es obligatorio", languageIndex));
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

  private void validateComplementaryTrainings(GraduateRequest request) {
    if (request.getFormacionesComplementarias() == null) {
      return;
    }

    int trainingIndex = 0;
    for (GraduateComplementaryTrainingRequest training : request.getFormacionesComplementarias()) {
      trainingIndex++;
      if (training != null && hasComplementaryTrainingData(training)) {
        if (StringUtils.isBlank(training.getNombreCurso())) {
          throw new GraduateException(
              String.format(
                  "El nombre del curso de la formacion %d es obligatorio", trainingIndex));
        }

        if (training.getFechaInicio() != null
            && training.getFechaFin() != null
            && training.getFechaFin().isBefore(training.getFechaInicio())) {
          throw new GraduateException(
              String.format(
                  "La fecha fin de la formacion %d no puede ser anterior a la fecha de inicio",
                  trainingIndex));
        }
      }
    }
  }

  private boolean hasComplementaryTrainingData(GraduateComplementaryTrainingRequest training) {
    return StringUtils.isNotBlank(training.getNombreCurso())
        || StringUtils.isNotBlank(training.getInstitucion())
        || training.getFechaInicio() != null
        || training.getFechaFin() != null;
  }

  private void validateWorkTrajectories(GraduateRequest request) {
    if (request.getTrayectoriasLaborales() == null) {
      return;
    }

    int activeJobs = 0;
    int trajectoryIndex = 0;
    for (GraduateWorkTrajectoryRequest trajectory : request.getTrayectoriasLaborales()) {
      trajectoryIndex++;
      if (!shouldValidateWorkTrajectory(trajectory)) {
        continue;
      }

      validateWorkTrajectoryRequiredFields(trajectory, trajectoryIndex);
      validateWorkTrajectoryDates(trajectory, trajectoryIndex);

      if (isActiveWorkTrajectory(trajectory)) {
        activeJobs++;
      }
    }

    if (activeJobs > 1) {
      throw new GraduateException("Solo se permite una trayectoria laboral activa");
    }
  }

  private boolean shouldValidateWorkTrajectory(GraduateWorkTrajectoryRequest trajectory) {
    return trajectory != null && hasWorkTrajectoryData(trajectory);
  }

  private void validateWorkTrajectoryRequiredFields(
      GraduateWorkTrajectoryRequest trajectory, int trajectoryIndex) {
    if (StringUtils.isBlank(trajectory.getEmpresa())) {
      throw new GraduateException(
          String.format("La empresa de la trayectoria %d es obligatoria", trajectoryIndex));
    }

    if (StringUtils.isBlank(trajectory.getCargo())) {
      throw new GraduateException(
          String.format("El cargo de la trayectoria %d es obligatorio", trajectoryIndex));
    }

    if (trajectory.getFechaInicio() == null) {
      throw new GraduateException(
          String.format("La fecha de inicio de la trayectoria %d es obligatoria", trajectoryIndex));
    }
  }

  private void validateWorkTrajectoryDates(
      GraduateWorkTrajectoryRequest trajectory, int trajectoryIndex) {
    if (trajectory.getFechaFin() != null
        && trajectory.getFechaFin().isBefore(trajectory.getFechaInicio())) {
      throw new GraduateException(
          String.format(
              "La fecha fin de la trayectoria %d no puede ser anterior a la fecha de inicio",
              trajectoryIndex));
    }
  }

  private boolean isActiveWorkTrajectory(GraduateWorkTrajectoryRequest trajectory) {
    return trajectory.getFechaFin() == null;
  }

  private boolean hasWorkTrajectoryData(GraduateWorkTrajectoryRequest trajectory) {
    return StringUtils.isNotBlank(trajectory.getEmpresa())
        || StringUtils.isNotBlank(trajectory.getCargo())
        || StringUtils.isNotBlank(trajectory.getModalidad())
        || trajectory.getFechaInicio() != null
        || trajectory.getFechaFin() != null;
  }
}
