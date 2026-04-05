package pe.com.graduate.insights.api.features.graduate.infrastructure.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.features.graduate.application.dto.CatalogOptionResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.FacultyCatalogResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.ProfessionalSchoolCatalogResponse;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.UniversityEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.DegreeTypeRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.FacultyRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.LanguageCatalogRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.ProfessionalSchoolRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.TitulationModeRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.UniversityRepository;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;

@RestController
@RequestMapping("/graduate/catalog")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('DIRECTOR', 'GRADUATE')")
public class GraduateCatalogController {

  private final FacultyRepository facultyRepository;
  private final ProfessionalSchoolRepository professionalSchoolRepository;
  private final DegreeTypeRepository degreeTypeRepository;
  private final TitulationModeRepository titulationModeRepository;
  private final LanguageCatalogRepository languageCatalogRepository;
  private final UniversityRepository universityRepository;

  @GetMapping("/faculties")
  public ResponseEntity<List<FacultyCatalogResponse>> listFaculties() {
    List<FacultyCatalogResponse> result =
        facultyRepository.findAllByEstadoOrderByNombreAsc(ConstantsUtils.STATUS_ACTIVE).stream()
            .map(
                faculty ->
                    FacultyCatalogResponse.builder()
                        .id(faculty.getId())
                        .nombre(faculty.getNombre())
                        .build())
            .toList();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/professional-schools")
  public ResponseEntity<List<ProfessionalSchoolCatalogResponse>> listProfessionalSchools(
      @RequestParam(value = "facultyId", required = false) Long facultyId) {
    List<ProfessionalSchoolCatalogResponse> result =
        (facultyId == null
                ? professionalSchoolRepository.findAllByEstadoOrderByNombreAsc(
                    ConstantsUtils.STATUS_ACTIVE)
                : professionalSchoolRepository.findAllByFacultadIdAndEstadoOrderByNombreAsc(
                    facultyId, ConstantsUtils.STATUS_ACTIVE))
            .stream()
                .map(
                    school ->
                        ProfessionalSchoolCatalogResponse.builder()
                            .id(school.getId())
                            .facultadId(school.getFacultadId())
                            .nombre(school.getNombre())
                            .build())
                .toList();

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/degree-types")
  public ResponseEntity<List<CatalogOptionResponse>> listDegreeTypes() {
    List<CatalogOptionResponse> result =
        degreeTypeRepository.findAllByEstadoOrderByNombreAsc(ConstantsUtils.STATUS_ACTIVE).stream()
            .map(
                item ->
                    CatalogOptionResponse.builder()
                        .id(item.getId())
                        .codigo(item.getCodigo())
                        .nombre(item.getNombre())
                        .build())
            .toList();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/titulation-modes")
  public ResponseEntity<List<CatalogOptionResponse>> listTitulationModes() {
    List<CatalogOptionResponse> result =
        titulationModeRepository
            .findAllByEstadoOrderByNombreAsc(ConstantsUtils.STATUS_ACTIVE)
            .stream()
            .map(
                item ->
                    CatalogOptionResponse.builder()
                        .id(item.getId())
                        .codigo(item.getCodigo())
                        .nombre(item.getNombre())
                        .build())
            .toList();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/languages")
  public ResponseEntity<List<CatalogOptionResponse>> listLanguages() {
    List<CatalogOptionResponse> result =
        languageCatalogRepository
            .findAllByEstadoOrderByNombreAsc(ConstantsUtils.STATUS_ACTIVE)
            .stream()
            .map(
                item ->
                    CatalogOptionResponse.builder()
                        .id(item.getId())
                        .codigo(item.getCodigo())
                        .nombre(item.getNombre())
                        .build())
            .toList();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/universities")
  public ResponseEntity<List<CatalogOptionResponse>> listUniversities() {
    List<CatalogOptionResponse> result =
        universityRepository.findAllByEstadoOrderByNombreAsc(ConstantsUtils.STATUS_ACTIVE).stream()
            .map(
                item ->
                    CatalogOptionResponse.builder()
                        .id(item.getId())
                        .codigo(null)
                        .nombre(item.getNombre())
                        .build())
            .toList();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/universities")
  public ResponseEntity<CatalogOptionResponse> createUniversity(
      @RequestBody Map<String, String> body) {
    String nombre = body.getOrDefault("nombre", "").trim();
    if (nombre.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return universityRepository
        .findByNombreIgnoreCase(nombre)
        .map(
            existing ->
                new ResponseEntity<>(
                    CatalogOptionResponse.builder()
                        .id(existing.getId())
                        .nombre(existing.getNombre())
                        .build(),
                    HttpStatus.OK))
        .orElseGet(
            () -> {
              UniversityEntity entity = new UniversityEntity();
              entity.setNombre(nombre);
              entity.setEstado(ConstantsUtils.STATUS_ACTIVE);
              UniversityEntity saved = universityRepository.save(entity);
              return new ResponseEntity<>(
                  CatalogOptionResponse.builder()
                      .id(saved.getId())
                      .nombre(saved.getNombre())
                      .build(),
                  HttpStatus.CREATED);
            });
  }
}
