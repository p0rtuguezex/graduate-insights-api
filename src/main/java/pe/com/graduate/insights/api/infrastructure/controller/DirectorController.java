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
import pe.com.graduate.insights.api.application.ports.input.DirectorUseCase;
import pe.com.graduate.insights.api.domain.models.request.DirectorRequest;
import pe.com.graduate.insights.api.domain.models.response.DirectorResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/director")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
@Tag(name = "Directores", description = "APIs para gestión de directores")
public class DirectorController {

  private final DirectorUseCase directorUseCase;
  private final PaginateMapper paginateMapper;

  @Operation(
      summary = "Obtener director por ID",
      description = "Obtiene un director específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Director encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = DirectorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Director no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<DirectorResponse>>
      getGraduate(
          @Parameter(description = "ID del director", required = true) @PathVariable Long id) {
    return ResponseUtils.successResponse(directorUseCase.getDomain(id));
  }

  @Operation(
      summary = "Crear nuevo director",
      description = "Crea un nuevo director con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Director creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "409", description = "Director ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      saveGraduate(
          @Parameter(description = "Datos del director", required = true) @Valid @RequestBody
              DirectorRequest directorRequest) {
    directorUseCase.save(directorRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar director",
      description = "Actualiza un director existente con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Director actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Director no encontrado")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateGraduate(
          @Parameter(description = "Datos del director", required = true) @RequestBody
              DirectorRequest directorRequest,
          @Parameter(description = "ID del director", required = true) @PathVariable Long id) {
    directorUseCase.update(directorRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Eliminar director",
      description = "Elimina un director específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Director eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Director no encontrado")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteGraduate(
          @Parameter(description = "ID del director", required = true) @PathVariable Long id) {
    directorUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar directores paginados",
      description = "Obtiene una lista paginada de directores con búsqueda opcional")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de directores obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = DirectorResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping
  ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<DirectorResponse>>>
      getListGraduatesPaginate(
          @Parameter(description = "Término de búsqueda", example = "juan")
              @RequestParam(value = "search", defaultValue = "")
              String search,
          @Parameter(description = "Número de página", example = "1")
              @RequestParam(value = "page", defaultValue = "1")
              String page,
          @Parameter(description = "Tamaño de página", example = "10")
              @RequestParam(value = "size", defaultValue = "10")
              String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<DirectorResponse> directorPage = directorUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        directorPage.getContent(), paginateMapper.toDomain(directorPage));
  }

  @Operation(
      summary = "Listar todos los directores",
      description = "Obtiene una lista completa de directores sin paginación")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de directores obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = KeyValueResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/list")
  public ResponseEntity<List<KeyValueResponse>>
      getListGraduateAll() {
      return new ResponseEntity<>(directorUseCase.getList(), HttpStatus.OK);
  }
}
