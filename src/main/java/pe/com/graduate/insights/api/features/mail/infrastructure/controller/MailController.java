package pe.com.graduate.insights.api.features.mail.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.features.mail.application.dto.ChangePasswordRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.MailRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.ValidateCodeRequest;
import pe.com.graduate.insights.api.features.mail.application.ports.input.MailUseCase;
import pe.com.graduate.insights.api.shared.models.response.ApiResponse;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;

@RestController
@RequestMapping("/mail")
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
