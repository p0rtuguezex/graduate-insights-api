package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.graduate.insights.api.application.ports.input.GraduateUseCase;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;

@RestController
@RequestMapping("/graduate")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class RegisterGraduateController {

  private final GraduateUseCase graduateUseCase;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> registerGraduate(
      @Valid @RequestBody GraduateRequest graduateRequest) {
    graduateUseCase.save(graduateRequest);
    return ResponseUtils.sucessCreateResponse();
  }
}
