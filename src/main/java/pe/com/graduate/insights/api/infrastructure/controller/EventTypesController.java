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
import pe.com.graduate.insights.api.application.ports.input.EventTypesUseCase;
import pe.com.graduate.insights.api.domain.models.request.EventTypesRequest;
import pe.com.graduate.insights.api.domain.models.response.EventTypesResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/event_types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
@Tag(name = "Tipos de Eventos", description = "APIs para gestión de tipos de eventos")
public class EventTypesController {

  private final EventTypesUseCase eventTypesUseCase;
  private final PaginateMapper paginateMapper;

  @Operation(
      summary = "Obtener tipo de evento por ID",
      description = "Obtiene un tipo de evento específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tipo de evento encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = EventTypesResponse.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de evento no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<EventTypesResponse>>
      getEducationCenter(
          @Parameter(description = "ID del tipo de evento", required = true) @PathVariable
              Long id) {
    return ResponseUtils.successResponse(eventTypesUseCase.getDomain(id));
  }

  @Operation(
      summary = "Crear nuevo tipo de evento",
      description = "Crea un nuevo tipo de evento con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Tipo de evento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "409", description = "Tipo de evento ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      saveEducationCenter(
          @Parameter(description = "Datos del tipo de evento", required = true) @Valid @RequestBody
              EventTypesRequest eventTypesRequest) {
    eventTypesUseCase.save(eventTypesRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar tipo de evento",
      description = "Actualiza un tipo de evento existente con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Tipo de evento actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Tipo de evento no encontrado")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateEducationCenter(
          @Parameter(description = "Datos del tipo de evento", required = true) @RequestBody
              EventTypesRequest eventTypesRequest,
          @Parameter(description = "ID del tipo de evento", required = true) @PathVariable
              Long id) {
    eventTypesUseCase.update(eventTypesRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Eliminar tipo de evento",
      description = "Elimina un tipo de evento específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Tipo de evento eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Tipo de evento no encontrado")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteEducationCenter(
          @Parameter(description = "ID del tipo de evento", required = true) @PathVariable
              Long id) {
    eventTypesUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar tipos de eventos paginados",
      description = "Obtiene una lista paginada de tipos de eventos con búsqueda opcional")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tipos de eventos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = EventTypesResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping
  ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<EventTypesResponse>>>
      getListEducationCenterPaginate(
          @Parameter(description = "Término de búsqueda", example = "conferencia")
              @RequestParam(value = "search", defaultValue = "")
              String search,
          @Parameter(description = "Número de página", example = "1")
              @RequestParam(value = "page", defaultValue = "1")
              String page,
          @Parameter(description = "Tamaño de página", example = "10")
              @RequestParam(value = "size", defaultValue = "10")
              String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<EventTypesResponse> eventTypesPage = eventTypesUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        eventTypesPage.getContent(), paginateMapper.toDomain(eventTypesPage));
  }

  @Operation(
      summary = "Listar todos los tipos de eventos",
      description = "Obtiene una lista completa de tipos de eventos sin paginación")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tipos de eventos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = KeyValueResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/list")
  public ResponseEntity<List<KeyValueResponse>>
      getListEducationCenterAll() {
      return new ResponseEntity<>(eventTypesUseCase.getList(), HttpStatus.OK);
  }
}
