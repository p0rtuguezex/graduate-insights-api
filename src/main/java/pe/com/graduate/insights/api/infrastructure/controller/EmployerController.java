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
import org.springframework.http.HttpStatus;
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
import pe.com.graduate.insights.api.application.ports.input.EmployerUseCase;
import pe.com.graduate.insights.api.domain.models.request.EmployerRequest;
import pe.com.graduate.insights.api.domain.models.response.EmployerResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/employer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
@Tag(name = "Empleadores", description = "APIs para gestión de empleadores")
public class EmployerController {

  private final EmployerUseCase employerUseCase;
  private final PaginateMapper paginateMapper;

  @Operation(
      summary = "Obtener empleador por ID",
      description = "Obtiene un empleador específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Empleador encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = EmployerResponse.class))),
        @ApiResponse(responseCode = "404", description = "Empleador no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<EmployerResponse>>
      getGraduate(
          @Parameter(description = "ID del empleador", required = true) @PathVariable Long id) {
    return ResponseUtils.successResponse(employerUseCase.getDomain(id));
  }

  @Operation(
      summary = "Crear nuevo empleador",
      description = "Crea un nuevo empleador con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Empleador creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "409", description = "Empleador ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      saveGraduate(
          @Parameter(description = "Datos del empleador", required = true) @Valid @RequestBody
              EmployerRequest employerRequest) {
    employerUseCase.save(employerRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar empleador",
      description = "Actualiza un empleador existente con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Empleador actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Empleador no encontrado")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateGraduate(
          @Parameter(description = "Datos del empleador", required = true) @RequestBody
              EmployerRequest employerRequest,
          @Parameter(description = "ID del empleador", required = true) @PathVariable Long id) {
    employerUseCase.update(employerRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Eliminar empleador",
      description = "Elimina un empleador específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Empleador eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Empleador no encontrado")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteGraduate(
          @Parameter(description = "ID del empleador", required = true) @PathVariable Long id) {
    employerUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar empleadores paginados",
      description = "Obtiene una lista paginada de empleadores con búsqueda opcional")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de empleadores obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = EmployerResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping
  ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<EmployerResponse>>>
      getListGraduatesPaginate(
          @Parameter(description = "Término de búsqueda", example = "microsoft")
              @RequestParam(value = "search", defaultValue = "")
              String search,
          @Parameter(description = "Número de página", example = "1")
              @RequestParam(value = "page", defaultValue = "1")
              String page,
          @Parameter(description = "Tamaño de página", example = "10")
              @RequestParam(value = "size", defaultValue = "10")
              String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<EmployerResponse> employerPage = employerUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        employerPage.getContent(), paginateMapper.toDomain(employerPage));
  }

  @Operation(
      summary = "Listar todos los empleadores",
      description = "Obtiene una lista completa de empleadores sin paginación")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de empleadores obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = KeyValueResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/list")
  public ResponseEntity<List<KeyValueResponse>> getListGraduateAll() {
    return new ResponseEntity<>(employerUseCase.getList(), HttpStatus.OK);
  }
}
