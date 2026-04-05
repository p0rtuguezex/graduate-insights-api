package pe.com.graduate.insights.api.features.user.infrastructure.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.features.auth.application.ports.input.CurrentUserUseCase;
import pe.com.graduate.insights.api.features.auth.domain.model.AuthenticatedUser;
import pe.com.graduate.insights.api.features.user.application.dto.ProfileUpdateRequest;
import pe.com.graduate.insights.api.features.user.application.dto.SelfPasswordChangeRequest;
import pe.com.graduate.insights.api.features.user.application.dto.UserDataResponse;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;
import pe.com.graduate.insights.api.features.user.application.ports.input.UserUseCase;
import pe.com.graduate.insights.api.features.userrole.application.ports.input.UserRoleUseCase;
import pe.com.graduate.insights.api.shared.models.response.ApiResponseWrapper;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('DIRECTOR', 'EMPLOYER', 'GRADUATE', 'USER')")
@Tag(
    name = "Perfil de Usuario",
    description = "APIs para gestión del perfil del usuario autenticado")
public class MeController {

  private final CurrentUserUseCase currentUserUseCase;
  private final UserUseCase userUseCase;
  private final UserRoleUseCase userRoleUseCase;

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
    var user = getAuthenticatedUser();
    UserDataResponse response =
        UserDataResponse.builder()
            .id(user.id())
            .name(user.firstName() + " " + user.lastName())
            .email(user.email())
            .genero(user.genero())
            .verified(user.verified())
            .role(userRoleUseCase.getUserRoleDisplayName(user.id()))
            .build();
    return ResponseEntity.ok(
        ApiResponseWrapper.success("Datos del usuario obtenidos exitosamente", response));
  }

  @Operation(
      summary = "Obtener el perfil detallado",
      description = "Retorna toda la información editable del usuario autenticado")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Perfil obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado")
      })
  @GetMapping("/me/profile")
  public ResponseEntity<ApiResponseWrapper<UserResponse>> getProfileDetails() {
    var user = getAuthenticatedUser();
    var profile = userUseCase.getDomain(user.id());
    return ResponseEntity.ok(ApiResponseWrapper.success("Perfil obtenido exitosamente", profile));
  }

  @Operation(summary = "Actualizar el perfil propio")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
      })
  @PutMapping("/me/profile")
  public ResponseEntity<ApiResponseWrapper<Void>> updateProfile(
      @Valid @RequestBody ProfileUpdateRequest request) {
    var user = getAuthenticatedUser();
    userUseCase.updateProfile(request, user.id());
    return ResponseEntity.ok(ApiResponseWrapper.success("Perfil actualizado", null));
  }

  @Operation(summary = "Actualizar contraseña propia")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Contraseña actualizada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
      })
  @PostMapping("/me/password")
  public ResponseEntity<ApiResponseWrapper<Void>> updatePassword(
      @Valid @RequestBody SelfPasswordChangeRequest request) {
    var user = getAuthenticatedUser();
    userUseCase.updatePassword(user.id(), request.getNewPassword());
    return ResponseEntity.ok(ApiResponseWrapper.success("Contraseña actualizada", null));
  }

  private AuthenticatedUser getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }

    return currentUserUseCase.getCurrentUser(authentication);
  }
}
