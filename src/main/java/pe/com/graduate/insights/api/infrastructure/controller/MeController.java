package pe.com.graduate.insights.api.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.AuthUseCase;
import pe.com.graduate.insights.api.application.service.UserRoleService;
import pe.com.graduate.insights.api.domain.models.response.ApiResponseWrapper;
import pe.com.graduate.insights.api.domain.models.response.UserDataResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasAnyRole('DIRECTOR', 'EMPLOYER', 'GRADUATE', 'USER')")
public class MeController {

  private final AuthUseCase authUseCase;
  private final UserRoleService userRoleService;

  @Operation(
      summary = "Obtener datos del usuario",
      description = "Obtiene los datos del usuario autenticado incluyendo su rol")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Datos del usuario obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = UserDataResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado")
      })
  @GetMapping("/me")
  public ResponseEntity<ApiResponseWrapper<UserDataResponse>> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }

    var user = authUseCase.getCurrentUser(authentication);
    UserDataResponse response =
        UserDataResponse.builder()
            .id(user.getId())
            .name(user.getNombres() + " " + user.getApellidos())
            .email(user.getCorreo())
            .verified(user.isVerificado())
            .role(userRoleService.getUserRoleDisplayName(user.getId()))
            .build();
    return ResponseEntity.ok(
        ApiResponseWrapper.success("Datos del usuario obtenidos exitosamente", response));
  }
}
