package pe.com.graduate.insights.api.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.com.graduate.insights.api.application.ports.input.GraduateUseCase;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/graduate")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
@Tag(name = "Graduados", description = "APIs para gestión de graduados y sus CV")
public class GraduateController {

  private final GraduateUseCase graduateUseCase;
  private final PaginateMapper paginateMapper;

  @Operation(
      summary = "Obtener graduado por ID",
      description = "Obtiene un graduado específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Graduado encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = GraduateResponse.class))),
        @ApiResponse(responseCode = "404", description = "Graduado no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<GraduateResponse>>
      getGraduate(
          @Parameter(description = "ID del graduado", required = true) @PathVariable Long id) {
    return ResponseUtils.successResponse(graduateUseCase.getDomain(id));
  }

  @Operation(
      summary = "Crear nuevo graduado",
      description = "Crea un nuevo graduado con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Graduado creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "409", description = "Graduado ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      saveGraduate(
          @Parameter(description = "Datos del graduado", required = true) @Valid @RequestBody
              GraduateRequest graduateRequest) {
    graduateUseCase.save(graduateRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar graduado",
      description =
          "Actualiza un graduado existente con la información proporcionada. Opcionalmente se puede incluir un archivo PDF como CV del graduado.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Graduado actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o archivo no válido"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Graduado no encontrado")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateGraduate(
          @Parameter(description = "Datos del graduado", required = true) @RequestBody
              GraduateRequest graduateRequest,
          @Parameter(description = "ID del graduado", required = true) @PathVariable Long id,
          @Parameter(description = "Archivo PDF del CV (opcional)")
              @RequestParam(value = "cvFile", required = false)
              MultipartFile cvFile) {

    // Actualizar datos del graduado
    graduateUseCase.update(graduateRequest, id);

    // Si se proporcionó un archivo CV, procesarlo
    if (cvFile != null && !cvFile.isEmpty()) {
      graduateUseCase.uploadCv(cvFile, id);
    }

    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Eliminar graduado",
      description = "Elimina un graduado específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Graduado eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Graduado no encontrado")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteGraduate(
          @Parameter(description = "ID del graduado", required = true) @PathVariable Long id) {
    graduateUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar graduados paginados",
      description = "Obtiene una lista paginada de graduados con búsqueda opcional")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de graduados obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = GraduateResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping
  ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<GraduateResponse>>>
      getListGraduatesPaginate(
          @Parameter(description = "Término de búsqueda", example = "maria")
              @RequestParam(value = "search", defaultValue = "")
              String search,
          @Parameter(description = "Número de página", example = "1")
              @RequestParam(value = "page", defaultValue = "1")
              String page,
          @Parameter(description = "Tamaño de página", example = "10")
              @RequestParam(value = "size", defaultValue = "10")
              String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<GraduateResponse> graduatePage = graduateUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        graduatePage.getContent(), paginateMapper.toDomain(graduatePage));
  }

  @Operation(
      summary = "Listar todos los graduados",
      description = "Obtiene una lista completa de graduados sin paginación")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de graduados obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = KeyValueResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/list")
  public ResponseEntity<List<KeyValueResponse>> getListGraduateAll() {
    return new ResponseEntity<>(graduateUseCase.getList(), HttpStatus.OK);
  }

  @Operation(
      summary = "Descargar CV del graduado",
      description = "Descarga el CV del graduado en formato PDF")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "CV descargado exitosamente",
            content = @Content(mediaType = "application/pdf")),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Graduado o CV no encontrado")
      })
  @GetMapping("/{id}/cv/download")
  public ResponseEntity<Resource> downloadCv(
      @Parameter(description = "ID del graduado", example = "1") @PathVariable Long id) {

    Resource resource = graduateUseCase.downloadCv(id);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cv_graduado_" + id + ".pdf\"")
        .body(resource);
  }
}
