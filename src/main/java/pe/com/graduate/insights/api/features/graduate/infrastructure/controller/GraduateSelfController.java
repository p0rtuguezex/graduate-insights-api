package pe.com.graduate.insights.api.features.graduate.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.features.auth.application.ports.input.AuthenticatedGraduateUseCase;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.features.graduate.application.ports.input.GraduateReadUseCase;
import pe.com.graduate.insights.api.features.graduate.application.ports.input.GraduateWriteUseCase;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;

@RestController
@RequestMapping("/graduate/me")
@RequiredArgsConstructor
@PreAuthorize("hasRole('GRADUATE')")
@Tag(name = "Perfil del Graduado", description = "APIs para que el graduado gestione su propio perfil")
public class GraduateSelfController {

  private final GraduateReadUseCase graduateReadUseCase;
  private final GraduateWriteUseCase graduateWriteUseCase;
  private final AuthenticatedGraduateUseCase authenticatedGraduateUseCase;

  @Operation(
      summary = "Obtener mi perfil de graduado",
      description = "Obtiene los datos completos del graduado autenticado")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Perfil obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = GraduateResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Graduado no encontrado")
      })
  @GetMapping
  public ResponseEntity<
          pe.com.graduate.insights.api.shared.models.response.ApiResponse<GraduateResponse>>
      getMyProfile() {
    Long graduateId = getAuthenticatedGraduateId();
    return ResponseUtils.successResponse(graduateReadUseCase.getDomain(graduateId));
  }

  @Operation(
      summary = "Actualizar mi perfil de graduado",
      description = "Actualiza los datos del graduado autenticado")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Graduado no encontrado")
      })
  @PutMapping
  public ResponseEntity<pe.com.graduate.insights.api.shared.models.response.ApiResponse<Void>>
      updateMyProfile(
          @Valid @RequestBody GraduateRequest graduateRequest) {
    Long graduateId = getAuthenticatedGraduateId();
    graduateWriteUseCase.update(graduateRequest, graduateId);
    return ResponseUtils.successUpdateResponse();
  }

  private Long getAuthenticatedGraduateId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }
    return authenticatedGraduateUseCase.getAuthenticatedGraduateId(authentication);
  }
}
