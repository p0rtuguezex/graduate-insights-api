package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.com.graduate.insights.api.application.ports.input.DirectorUseCase;
import pe.com.graduate.insights.api.domain.models.request.DirectorRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.DirectorResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/director")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
@PreAuthorize("hasRole('USER')")
public class DirectorController {

  private final DirectorUseCase directorUseCase;

  private final PaginateMapper paginateMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<DirectorResponse>> getGraduate(@PathVariable Long id) {
    return ResponseUtils.successResponse(directorUseCase.getDomain(id));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveGraduate(
      @Valid @RequestBody DirectorRequest directorRequest) {
    directorUseCase.save(directorRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateGraduate(
      @RequestBody DirectorRequest directorRequest, @PathVariable Long id) {
    directorUseCase.update(directorRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteGraduate(@PathVariable Long id) {
    directorUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<DirectorResponse>>> getListGraduatesPaginate(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<DirectorResponse> directorPage = directorUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        directorPage.getContent(), paginateMapper.toDomain(directorPage));
  }

  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<DirectorResponse>>> getListGraduateAll() {
    return ResponseUtils.successResponse(directorUseCase.getList());
  }
}
