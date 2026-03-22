package pe.com.graduate.insights.api.features.graduatedashboard.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;
import pe.com.graduate.insights.api.features.auth.application.ports.input.AuthenticatedGraduateUseCase;
import pe.com.graduate.insights.api.features.graduatedashboard.application.dto.GraduateDashboardResponse;
import pe.com.graduate.insights.api.features.graduatedashboard.application.ports.input.GraduateDashboardUseCase;

@RestController
@RequestMapping("/graduate-dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('GRADUATE')")
@Tag(
    name = "Dashboard del Graduado",
    description = "Datos agregados para el dashboard del graduado")
public class GraduateDashboardController {

  private final GraduateDashboardUseCase graduateDashboardUseCase;
  private final AuthenticatedGraduateUseCase authenticatedGraduateUseCase;

  @Operation(
      summary = "Obtener dashboard del graduado",
      description =
          "Entrega estadísticas, encuestas, trabajos y ofertas relevantes para el graduado autenticado")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Dashboard obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = GraduateDashboardResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Graduado no encontrado")
      })
  @GetMapping
  public ResponseEntity<
          pe.com.graduate.insights.api.shared.models.response.ApiResponse<
              GraduateDashboardResponse>>
      getDashboard() {
    Long graduateId = getAuthenticatedGraduateId();
    GraduateDashboardResponse dashboard = graduateDashboardUseCase.getDashboard(graduateId);
    return ResponseUtils.successResponse(dashboard);
  }

  private Long getAuthenticatedGraduateId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }
    return authenticatedGraduateUseCase.getAuthenticatedGraduateId(authentication);
  }
}


