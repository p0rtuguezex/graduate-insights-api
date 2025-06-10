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
import pe.com.graduate.insights.api.application.ports.input.GraduateUseCase;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/graduate")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
@PreAuthorize("hasRole('USER')")
public class GraduateController {

  private final GraduateUseCase graduateUseCase;

  private final PaginateMapper paginateMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<GraduateResponse>> getGraduate(@PathVariable Long id) {
    return ResponseUtils.successResponse(graduateUseCase.getDomain(id));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveGraduate(
      @Valid @RequestBody GraduateRequest graduateRequest) {
    graduateUseCase.save(graduateRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateGraduate(
      @RequestBody GraduateRequest graduateRequest, @PathVariable Long id) {
    graduateUseCase.update(graduateRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteGraduate(@PathVariable Long id) {
    graduateUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<GraduateResponse>>> getListGraduatesPaginate(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<GraduateResponse> graduatePage = graduateUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        graduatePage.getContent(), paginateMapper.toDomain(graduatePage));
  }

  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<GraduateResponse>>> getListGraduateAll() {
    return ResponseUtils.successResponse(graduateUseCase.getList());
  }
}
