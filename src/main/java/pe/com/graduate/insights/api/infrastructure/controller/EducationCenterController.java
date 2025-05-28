package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import pe.com.graduate.insights.api.application.ports.input.EducationCenterUseCase;
import pe.com.graduate.insights.api.domain.models.request.EducationCenterRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.EducationCenterResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/education_center")
@RequiredArgsConstructor
public class EducationCenterController {

  private final EducationCenterUseCase educationCenterUseCase;

  private final PaginateMapper paginateMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<EducationCenterResponse>> getEducationCenter(
      @PathVariable Long id) {
    return ResponseUtils.successResponse(educationCenterUseCase.getDomain(id));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveEducationCenter(
      @Valid @RequestBody EducationCenterRequest educationCenterRequest) {
    educationCenterUseCase.save(educationCenterRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateEducationCenter(
      @RequestBody EducationCenterRequest educationCenterRequest, @PathVariable Long id) {
    educationCenterUseCase.update(educationCenterRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteEducationCenter(@PathVariable Long id) {
    educationCenterUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<EducationCenterResponse>>> getListEducationCenterPaginate(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<EducationCenterResponse> educationCenterPage =
        educationCenterUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        educationCenterPage.getContent(), paginateMapper.toDomain(educationCenterPage));
  }

  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<EducationCenterResponse>>> getListEducationCenterAll() {
    return ResponseUtils.successResponse(educationCenterUseCase.getList());
  }
}
