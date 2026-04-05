package pe.com.graduate.insights.api.features.employer.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.features.employer.application.dto.EmployerRequest;
import pe.com.graduate.insights.api.features.employer.application.ports.input.EmployerUseCase;
import pe.com.graduate.insights.api.shared.models.response.ApiResponse;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;

@RestController
@RequestMapping("/employer")
@RequiredArgsConstructor
public class RegisterEmployerController {

  private final EmployerUseCase employerUseCase;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> registerEmployer(
      @Valid @RequestBody EmployerRequest employerRequest) {
    employerUseCase.save(employerRequest);
    return ResponseUtils.sucessCreateResponse();
  }
}
