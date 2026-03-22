package pe.com.graduate.insights.api.features.director.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.shared.models.response.ApiResponse;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;
import pe.com.graduate.insights.api.features.director.application.dto.DirectorRequest;
import pe.com.graduate.insights.api.features.director.application.ports.input.DirectorUseCase;

@RestController
@RequestMapping("/director")
@RequiredArgsConstructor
public class RegisterDirectorController {

  private final DirectorUseCase directorUseCase;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> registerDirector(
      @Valid @RequestBody DirectorRequest directorRequest) {
    directorUseCase.save(directorRequest);
    return ResponseUtils.sucessCreateResponse();
  }
}


