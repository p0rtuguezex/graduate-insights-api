package pe.com.graduate.insights.api.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.UserUseCase;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.UserResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserUseCase userUseCase;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
    return ResponseUtils.successResponse(userUseCase.getDomain(id));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveUser(@RequestBody UserRequest userRequest) {
    userUseCase.save(userRequest);
    return ResponseUtils.createSucessResponse();
  }
}
