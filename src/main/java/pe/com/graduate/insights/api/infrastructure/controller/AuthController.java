package pe.com.graduate.insights.api.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.AuthUseCase;
import pe.com.graduate.insights.api.domain.models.request.LoginRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponseWrapper;
import pe.com.graduate.insights.api.infrastructure.security.JwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
@Tag(name = "Autenticación", description = "APIs para autenticación y registro de usuarios")
public class AuthController {

  private final AuthUseCase authUseCase;
  private final JwtService jwtService;

  @RequestMapping(method = RequestMethod.OPTIONS)
  public ResponseEntity<Void> handleOptions() {
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "Iniciar sesión",
      description = "Autentica un usuario y devuelve un token JWT")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
      })
  @PostMapping("/login")
  public ResponseEntity<ApiResponseWrapper<String>> login(
      @Parameter(description = "Credenciales de acceso", required = true) @Valid @RequestBody
          LoginRequest request) {
    try {
      var user = authUseCase.login(request.getEmail(), request.getPassword());
      var token = jwtService.generateToken(user);
      var response = ApiResponseWrapper.success("Login exitoso", token);
      
      // Log para debugging
      System.out.println("LOGIN EXITOSO - Usuario: " + user.getUsername() + ", Token generado: " + (token != null ? "SÍ" : "NO"));
      System.out.println("Respuesta: " + response);
      
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      System.out.println("ERROR EN LOGIN: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
