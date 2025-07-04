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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.SurveyUseCase;
import pe.com.graduate.insights.api.domain.models.request.SurveyRequest;
import pe.com.graduate.insights.api.domain.models.response.SurveyResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
@Tag(name = "Encuestas", description = "APIs para gestión de encuestas")
public class SurveyController {

  private final SurveyUseCase surveyUseCase;
  private final PaginateMapper paginateMapper;

  @Operation(
      summary = "Obtener encuesta por ID",
      description = "Obtiene una encuesta específica por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Encuesta encontrada exitosamente",
            content = @Content(schema = @Schema(implementation = SurveyResponse.class))),
        @ApiResponse(responseCode = "404", description = "Encuesta no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<SurveyResponse>>
      getSurvey(
          @Parameter(description = "ID de la encuesta", required = true) @PathVariable Long id) {
    return ResponseUtils.successResponse(surveyUseCase.getDomain(id));
  }

  @Operation(
      summary = "Crear nueva encuesta",
      description = "Crea una nueva encuesta con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Encuesta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "409", description = "Encuesta ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      saveSurvey(
          @Parameter(description = "Datos de la encuesta", required = true) @Valid @RequestBody
              SurveyRequest surveyRequest) {
    surveyUseCase.save(surveyRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar encuesta",
      description = "Actualiza una encuesta existente con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Encuesta actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Encuesta no encontrada")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateSurvey(
          @Parameter(description = "Datos de la encuesta", required = true) @RequestBody
              SurveyRequest surveyRequest,
          @Parameter(description = "ID de la encuesta", required = true) @PathVariable Long id) {
    surveyUseCase.update(surveyRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Eliminar encuesta",
      description = "Elimina una encuesta específica por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Encuesta eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Encuesta no encontrada")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteSurvey(
          @Parameter(description = "ID de la encuesta", required = true) @PathVariable Long id) {
    surveyUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar encuestas paginadas",
      description = "Obtiene una lista paginada de encuestas con búsqueda opcional")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de encuestas obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = SurveyResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping
  ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<SurveyResponse>>>
      getListSurveysPaginate(
          @Parameter(description = "Término de búsqueda", example = "satisfacción")
              @RequestParam(value = "search", defaultValue = "")
              String search,
          @Parameter(description = "Número de página", example = "1")
              @RequestParam(value = "page", defaultValue = "1")
              String page,
          @Parameter(description = "Tamaño de página", example = "10")
              @RequestParam(value = "size", defaultValue = "10")
              String size,
          @Parameter(description = "Estado de la encuesta", example = "1")
              @RequestParam(value = "status", required = false)
              String status) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<SurveyResponse> surveyPage = surveyUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        surveyPage.getContent(), paginateMapper.toDomain(surveyPage));
  }

  @Operation(
      summary = "Actualizar estado de encuesta",
      description = "Actualiza el estado de una encuesta específica")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estado de encuesta actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Encuesta no encontrada")
      })
  @PatchMapping("/{id}/status")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateSurveyStatus(
          @Parameter(description = "ID de la encuesta", required = true) @PathVariable Long id,
          @Parameter(description = "Nuevo estado", required = true, example = "1")
              @RequestParam("status")
              String status) {
    surveyUseCase.updateStatus(id, status);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Obtener encuestas activas",
      description = "Obtiene todas las encuestas que están activas")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de encuestas activas obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = SurveyResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/active")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<SurveyResponse>>>
      getActiveSurveys() {
    List<SurveyResponse> activeSurveys = surveyUseCase.getActiveSurveys();
    return ResponseUtils.successResponse(activeSurveys);
  }

  @Operation(
      summary = "Obtener encuestas por estado",
      description = "Obtiene todas las encuestas que tienen un estado específico")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de encuestas obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = SurveyResponse.class))),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/status/{status}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<SurveyResponse>>>
      getSurveysByStatus(
          @Parameter(description = "Estado de las encuestas", required = true, example = "1")
              @PathVariable
              String status) {
    List<SurveyResponse> surveys = surveyUseCase.getSurveysByStatus(status);
    return ResponseUtils.successResponse(surveys);
  }
}
