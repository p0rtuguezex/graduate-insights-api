package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.GraduateSelfRegistrationUseCase;
import pe.com.graduate.insights.api.domain.models.request.GraduateSelfRegistrationRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponseWrapper;

@RestController
@RequestMapping("/graduate")
@RequiredArgsConstructor
public class RegisterGraduateController {

  private final GraduateSelfRegistrationUseCase graduateSelfRegistrationUseCase;

  @PostMapping("/register")
  public ResponseEntity<ApiResponseWrapper<Void>> registerGraduate(
      @Valid @RequestBody GraduateSelfRegistrationRequest request) {
    graduateSelfRegistrationUseCase.register(request);
    ApiResponseWrapper<Void> response =
        ApiResponseWrapper.success(
            "Registro recibido. Revisa tu correo para validar tu cuenta y espera la aprobación del director.",
            null);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
