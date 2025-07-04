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
import pe.com.graduate.insights.api.application.ports.input.SurveyTypeUseCase;
import pe.com.graduate.insights.api.domain.models.request.SurveyTypeRequest;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyTypeResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/survey-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
@Tag(name = "Tipos de Encuesta", description = "APIs para gestión de tipos de encuesta")
public class SurveyTypeController {

  private final SurveyTypeUseCase surveyTypeUseCase;
  private final PaginateMapper paginateMapper;

  @Operation(
      summary = "Obtener tipo de encuesta por ID",
      description = "Obtiene un tipo de encuesta específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tipo de encuesta encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = SurveyTypeResponse.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de encuesta no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<SurveyTypeResponse>>
      getSurveyType(
          @Parameter(description = "ID del tipo de encuesta", required = true) @PathVariable
              Long id) {
    return ResponseUtils.successResponse(surveyTypeUseCase.getDomain(id));
  }

  @Operation(
      summary = "Crear nuevo tipo de encuesta",
      description = "Crea un nuevo tipo de encuesta con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Tipo de encuesta creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "409", description = "Tipo de encuesta ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      saveSurveyType(
          @Parameter(description = "Datos del tipo de encuesta", required = true)
              @Valid
              @RequestBody
              SurveyTypeRequest surveyTypeRequest) {
    surveyTypeUseCase.save(surveyTypeRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar tipo de encuesta",
      description = "Actualiza un tipo de encuesta existente con la información proporcionada")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tipo de encuesta actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Tipo de encuesta no encontrado")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateSurveyType(
          @Parameter(description = "Datos del tipo de encuesta", required = true) @RequestBody
              SurveyTypeRequest surveyTypeRequest,
          @Parameter(description = "ID del tipo de encuesta", required = true) @PathVariable
              Long id) {
    surveyTypeUseCase.update(surveyTypeRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Eliminar tipo de encuesta",
      description = "Elimina un tipo de encuesta específico por su ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Tipo de encuesta eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Tipo de encuesta no encontrado")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteSurveyType(
          @Parameter(description = "ID del tipo de encuesta", required = true) @PathVariable
              Long id) {
    surveyTypeUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar tipos de encuesta paginados",
      description = "Obtiene una lista paginada de tipos de encuesta con búsqueda opcional")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tipos de encuesta obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = SurveyTypeResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping
  ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<SurveyTypeResponse>>>
      getListSurveyTypesPaginate(
          @Parameter(description = "Término de búsqueda", example = "satisfacción")
              @RequestParam(value = "search", defaultValue = "")
              String search,
          @Parameter(description = "Número de página", example = "1")
              @RequestParam(value = "page", defaultValue = "1")
              String page,
          @Parameter(description = "Tamaño de página", example = "10")
              @RequestParam(value = "size", defaultValue = "10")
              String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<SurveyTypeResponse> surveyTypePage = surveyTypeUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        surveyTypePage.getContent(), paginateMapper.toDomain(surveyTypePage));
  }

  @Operation(
      summary = "Listar todos los tipos de encuesta",
      description = "Obtiene una lista completa de tipos de encuesta sin paginación")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tipos de encuesta obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = KeyValueResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/list")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<KeyValueResponse>>>
      getListSurveyTypeAll() {
    return ResponseUtils.successResponse(surveyTypeUseCase.getList());
  }

  @Operation(
      summary = "Obtener tipos de encuesta activos",
      description = "Obtiene todos los tipos de encuesta que están activos")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tipos de encuesta activos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = SurveyTypeResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/active")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<SurveyTypeResponse>>>
      getActiveSurveyTypes() {
    return ResponseUtils.successResponse(surveyTypeUseCase.getActiveTypes());
  }
}
