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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import pe.com.graduate.insights.api.application.ports.input.EducationCenterUseCase;
import pe.com.graduate.insights.api.domain.models.request.EducationCenterRequest;
import pe.com.graduate.insights.api.domain.models.response.EducationCenterResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/education_center")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
@Tag(name = "Centros Educativos", description = "APIs para gestión de centros educativos")
public class EducationCenterController {

  private final EducationCenterUseCase educationCenterUseCase;
  private final PaginateMapper paginateMapper;

  @Operation(
      summary = "Obtener centro educativo por ID",
      description = "Obtiene un centro educativo específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Centro educativo encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = EducationCenterResponse.class))),
        @ApiResponse(responseCode = "404", description = "Centro educativo no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<EducationCenterResponse>>
      getEducationCenter(
          @Parameter(description = "ID del centro educativo", required = true) @PathVariable
              Long id) {
    return ResponseUtils.successResponse(educationCenterUseCase.getDomain(id));
  }

  @Operation(
      summary = "Crear nuevo centro educativo",
      description = "Crea un nuevo centro educativo con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Centro educativo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "409", description = "Centro educativo ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      saveEducationCenter(
          @Parameter(description = "Datos del centro educativo", required = true)
              @Valid
              @RequestBody
              EducationCenterRequest educationCenterRequest) {
    educationCenterUseCase.save(educationCenterRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar centro educativo",
      description = "Actualiza un centro educativo existente con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Centro educativo actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Centro educativo no encontrado")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateEducationCenter(
          @Parameter(description = "Datos del centro educativo", required = true) @RequestBody
              EducationCenterRequest educationCenterRequest,
          @Parameter(description = "ID del centro educativo", required = true) @PathVariable
              Long id) {
    educationCenterUseCase.update(educationCenterRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Eliminar centro educativo",
      description = "Elimina un centro educativo específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Centro educativo eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Centro educativo no encontrado")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteEducationCenter(
          @Parameter(description = "ID del centro educativo", required = true) @PathVariable
              Long id) {
    educationCenterUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar centros educativos paginados",
      description = "Obtiene una lista paginada de centros educativos con búsqueda opcional")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de centros educativos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = EducationCenterResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping
  ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<
              List<EducationCenterResponse>>>
      getListEducationCenterPaginate(
          @Parameter(description = "Término de búsqueda", example = "universidad")
              @RequestParam(value = "search", defaultValue = "")
              String search,
          @Parameter(description = "Número de página", example = "1")
              @RequestParam(value = "page", defaultValue = "1")
              String page,
          @Parameter(description = "Tamaño de página", example = "10")
              @RequestParam(value = "size", defaultValue = "10")
              String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<EducationCenterResponse> educationCenterPage =
        educationCenterUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        educationCenterPage.getContent(), paginateMapper.toDomain(educationCenterPage));
  }

  @Operation(
      summary = "Listar todos los centros educativos",
      description = "Obtiene una lista completa de centros educativos sin paginación")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de centros educativos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = EducationCenterResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/list")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<
              List<EducationCenterResponse>>>
      getListEducationCenterAll() {
    return ResponseUtils.successResponse(educationCenterUseCase.getList());
  }
}
