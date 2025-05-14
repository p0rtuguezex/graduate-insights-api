package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateUser(
      @Valid @RequestBody UserRequest userRequest, @PathVariable Long id) {
    userUseCase.update(userRequest, id);
    return ResponseUtils.successResponse();
  }

  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<UserResponse>>> userList() {
    return ResponseUtils.successResponse(userUseCase.getList());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
    userUseCase.delete(id);
    return ResponseUtils.deleteSuccessResponse();
  }

  @GetMapping("/list-pageble")
  public ResponseEntity<ApiResponse<List<UserResponse>>> userListPageable(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    var userPage = userUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponse(userPage.getContent());
  }
}
