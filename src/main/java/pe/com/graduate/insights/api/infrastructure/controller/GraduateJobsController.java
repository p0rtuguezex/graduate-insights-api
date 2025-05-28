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
import pe.com.graduate.insights.api.application.ports.input.GraduateJobsUseCase;
import pe.com.graduate.insights.api.domain.models.request.GraduateJobRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.GraduateJobResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/graduate/{graduateId}/jobs")
@RequiredArgsConstructor
public class GraduateJobsController {

  private final GraduateJobsUseCase graduateJobsUseCase;

  private final PaginateMapper paginateMapper;

  @GetMapping
  public ResponseEntity<ApiResponse<List<GraduateJobResponse>>> getJobsListByGraduateId(
      @PathVariable Long graduateId) {
    return ResponseUtils.successResponse(graduateJobsUseCase.getJobsList(graduateId));
  }

  @GetMapping("/{jobId}")
  public ResponseEntity<ApiResponse<GraduateJobResponse>> getJobsByGraduateId(
      @PathVariable Long graduateId, @PathVariable Long jobId) {
    return ResponseUtils.successResponse(graduateJobsUseCase.getDomain(graduateId, jobId));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createJob(
      @Valid @RequestBody GraduateJobRequest graduateJobRequest, @PathVariable Long graduateId) {
    graduateJobsUseCase.save(graduateJobRequest, graduateId);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{jobId}")
  public ResponseEntity<ApiResponse<Void>> updateJob(
      @RequestBody GraduateJobRequest graduateJobRequest,
      @PathVariable Long graduateId,
      @PathVariable Long jobId) {
    graduateJobsUseCase.update(graduateJobRequest, graduateId, jobId);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{jobId}")
  public ResponseEntity<ApiResponse<Void>> deleteJob(
      @PathVariable Long graduateId, @PathVariable Long jobId) {
    graduateJobsUseCase.delete(graduateId, jobId);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping("/pagination")
  ResponseEntity<ApiResponse<List<GraduateJobResponse>>> getGraduatesJobsPaginated(
      @PathVariable Long graduateId,
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<GraduateJobResponse> graduateJobPage =
        graduateJobsUseCase.getPagination(search, pageable, graduateId);
    return ResponseUtils.successResponsePaginate(
        graduateJobPage.getContent(), paginateMapper.toDomain(graduateJobPage));
  }
}
