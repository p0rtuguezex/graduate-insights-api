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
import pe.com.graduate.insights.api.application.ports.input.EventUseCase;
import pe.com.graduate.insights.api.domain.models.request.EventRequest;
import pe.com.graduate.insights.api.domain.models.response.EventResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
@Tag(name = "Eventos", description = "APIs para gestión de eventos")
public class EventController {

  private final EventUseCase eventUseCase;
  private final PaginateMapper paginateMapper;

  @Operation(
      summary = "Obtener evento por ID",
      description = "Obtiene un evento específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Evento encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = EventResponse.class))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<EventResponse>>
      getEvent(@Parameter(description = "ID del evento", required = true) @PathVariable Long id) {
    return ResponseUtils.successResponse(eventUseCase.getDomain(id));
  }

  @Operation(
      summary = "Crear nuevo evento",
      description = "Crea un nuevo evento con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "409", description = "Evento ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      saveEvent(
          @Parameter(description = "Datos del evento", required = true) @Valid @RequestBody
              EventRequest eventRequest) {
    eventUseCase.save(eventRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar evento",
      description = "Actualiza un evento existente con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateEvent(
          @Parameter(description = "Datos del evento", required = true) @RequestBody
              EventRequest eventRequest,
          @Parameter(description = "ID del evento", required = true) @PathVariable Long id) {
    eventUseCase.update(eventRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(summary = "Eliminar evento", description = "Elimina un evento específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Evento eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteEvent(
          @Parameter(description = "ID del evento", required = true) @PathVariable Long id) {
    eventUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar eventos paginados",
      description = "Obtiene una lista paginada de eventos con búsqueda opcional")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de eventos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = EventResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado")
      })
  @GetMapping
  ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<EventResponse>>>
      getListEventsPaginate(
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
    Page<EventResponse> eventPage = eventUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        eventPage.getContent(), paginateMapper.toDomain(eventPage));
  }

  @Operation(
      summary = "Listar todos los eventos",
      description = "Obtiene una lista completa de eventos sin paginación")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de eventos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = KeyValueResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado")
      })
  @GetMapping("/list")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<KeyValueResponse>>>
      getListEventsAll() {
    return ResponseUtils.successResponse(eventUseCase.getList());
  }
}
