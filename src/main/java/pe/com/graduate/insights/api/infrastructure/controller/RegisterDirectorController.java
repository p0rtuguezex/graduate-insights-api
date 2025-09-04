package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.graduate.insights.api.application.ports.input.DirectorUseCase;
import pe.com.graduate.insights.api.domain.models.request.DirectorRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;

@RestController
@RequestMapping("/director")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:3000}")
public class RegisterDirectorController {

  private final DirectorUseCase directorUseCase;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> registerDirector(
      @Valid @RequestBody DirectorRequest directorRequest) {
    directorUseCase.save(directorRequest);
    return ResponseUtils.sucessCreateResponse();
  }
}
