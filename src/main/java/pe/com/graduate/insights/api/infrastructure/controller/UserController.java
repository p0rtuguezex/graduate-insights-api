package pe.com.graduate.insights.api.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.UserUseCase;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.User;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserUseCase userUseCase;

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userUseCase
        .getDomain(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PostMapping
  public ResponseEntity<Void> saveUser(@RequestBody UserRequest userRequest) {
    userUseCase.save(userRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
