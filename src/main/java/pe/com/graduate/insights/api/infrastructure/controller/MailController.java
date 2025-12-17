package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.graduate.insights.api.application.ports.input.MailUseCase;
import pe.com.graduate.insights.api.domain.models.request.ChangePasswordRequest;
import pe.com.graduate.insights.api.domain.models.request.MailRequest;
import pe.com.graduate.insights.api.domain.models.request.ValidateCodeRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;

@RestController
@RequestMapping("/mail")
@CrossOrigin(origins = "${cors.allowed-origins}")
@RequiredArgsConstructor
public class MailController {

  private final MailUseCase mailUseCase;

  @PostMapping("/send-code")
  public ResponseEntity<ApiResponse<Void>> sendCode(@Valid @RequestBody MailRequest mailRequest) {
    mailUseCase.sendCode(mailRequest);
    return ResponseUtils.successResponse();
  }

  @PostMapping("/validate-code")
  public ResponseEntity<ApiResponse<Void>> validateCode(
      @Valid @RequestBody ValidateCodeRequest validateCodeRequest) {
    mailUseCase.validateCode(validateCodeRequest);
    return ResponseUtils.successResponse();
  }

  @PostMapping("/change-password")
  public ResponseEntity<ApiResponse<Void>> changePassword(
      @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
    mailUseCase.changePassword(changePasswordRequest);
    return ResponseUtils.successResponse();
  }
}
