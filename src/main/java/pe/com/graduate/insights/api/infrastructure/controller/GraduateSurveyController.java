package pe.com.graduate.insights.api.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.AuthUseCase;
import pe.com.graduate.insights.api.application.ports.input.GraduateSurveyUseCase;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.response.ApiResponseWrapper;
import pe.com.graduate.insights.api.domain.models.response.GraduateSurveyDetailResponse;
import pe.com.graduate.insights.api.domain.models.response.GraduateSurveyListResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateRepository;

@RestController
@RequestMapping("/graduate-surveys")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('GRADUATE')")
public class GraduateSurveyController {

  private final AuthUseCase authUseCase;
  private final GraduateSurveyUseCase graduateSurveyUseCase;
  private final GraduateRepository graduateRepository;

  @Operation(
      summary = "Obtener encuestas del graduado",
      description =
          "Obtiene las encuestas disponibles para el graduado autenticado con filtros opcionales")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Encuestas obtenidas exitosamente",
            content =
                @Content(schema = @Schema(implementation = GraduateSurveyListResponse.class))),
        @ApiResponse(responseCode = "400", description = "Filtro inválido"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Graduado no encontrado")
      })
  @GetMapping
  public ResponseEntity<ApiResponseWrapper<List<GraduateSurveyListResponse>>> getSurveys(
      @Parameter(
              description =
                  "Filtro para el estado de las encuestas. Valores válidos: 'all' (todas), 'completed' (completadas), 'pending' (pendientes)",
              example = "all")
          @RequestParam(value = "filter", defaultValue = "all")
          String filter) {

    Long graduateId = getAuthenticatedGraduateId();
    List<GraduateSurveyListResponse> surveys;
    String message;

    switch (filter.toLowerCase()) {
      case "completed":
        surveys = graduateSurveyUseCase.getCompletedSurveysForGraduate(graduateId);
        message = "Encuestas completadas obtenidas exitosamente";
        break;
      case "pending":
        surveys = graduateSurveyUseCase.getPendingSurveysForGraduate(graduateId);
        message = "Encuestas pendientes obtenidas exitosamente";
        break;
      case "all":
      default:
        surveys = graduateSurveyUseCase.getAllSurveysForGraduate(graduateId);
        message = "Todas las encuestas obtenidas exitosamente";
        break;
    }

    return ResponseEntity.ok(ApiResponseWrapper.success(message, surveys));
  }

  @Operation(
      summary = "Obtener detalle de una encuesta",
      description =
          "Obtiene el detalle completo de una encuesta específica con preguntas y respuestas (si las hay) del graduado autenticado")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Detalle de la encuesta obtenido exitosamente",
            content =
                @Content(schema = @Schema(implementation = GraduateSurveyDetailResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Encuesta o graduado no encontrado")
      })
  @GetMapping("/{surveyId}")
  public ResponseEntity<ApiResponseWrapper<GraduateSurveyDetailResponse>> getSurveyDetail(
      @Parameter(description = "ID de la encuesta", required = true) @PathVariable Long surveyId) {

    Long graduateId = getAuthenticatedGraduateId();
    GraduateSurveyDetailResponse surveyDetail =
        graduateSurveyUseCase.getSurveyDetailForGraduate(surveyId, graduateId);

    return ResponseEntity.ok(
        ApiResponseWrapper.success("Detalle de la encuesta obtenido exitosamente", surveyDetail));
  }

  private Long getAuthenticatedGraduateId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }

    var user = authUseCase.getCurrentUser(authentication);

    // Obtener el graduado asociado al usuario autenticado
    var graduate =
        graduateRepository
            .findByUserIdAndUserEstado(user.getId(), ConstantsUtils.STATUS_ACTIVE)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "El usuario autenticado no es un graduado o no está activo"));

    return graduate.getId();
  }
}
