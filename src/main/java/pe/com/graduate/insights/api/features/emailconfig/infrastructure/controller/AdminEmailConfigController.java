package pe.com.graduate.insights.api.features.emailconfig.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigRequest;
import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailConfigResponse;
import pe.com.graduate.insights.api.features.emailconfig.application.dto.EmailTestRequest;
import pe.com.graduate.insights.api.features.emailconfig.application.ports.input.EmailConfigUseCase;
import pe.com.graduate.insights.api.shared.models.response.ApiResponse;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;

@RestController
@RequestMapping("/admin/email-config")
@PreAuthorize("hasRole('DIRECTOR')")
@RequiredArgsConstructor
@Tag(name = "Email Config", description = "Configuracion de email para administradores")
public class AdminEmailConfigController {

  private final EmailConfigUseCase emailConfigUseCase;

  @GetMapping
  @Operation(summary = "Obtener configuracion de email activa")
  public ResponseEntity<ApiResponse<EmailConfigResponse>> getConfig() {
    return ResponseUtils.successResponse(emailConfigUseCase.getActiveConfig());
  }

  @PutMapping
  @Operation(summary = "Crear o actualizar configuracion de email")
  public ResponseEntity<ApiResponse<Void>> updateConfig(
      @Valid @RequestBody EmailConfigRequest request) {
    emailConfigUseCase.saveOrUpdate(request);
    return ResponseUtils.successUpdateResponse();
  }

  @PostMapping("/test")
  @Operation(summary = "Enviar correo de prueba a un destinatario")
  public ResponseEntity<ApiResponse<Void>> sendTestEmail(
      @Valid @RequestBody EmailTestRequest request) {
    emailConfigUseCase.sendTestEmail(request.getEmail());
    return ResponseUtils.successResponse(null);
  }
}
