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
import pe.com.graduate.insights.api.application.ports.input.JobUseCase;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
@PreAuthorize("hasRole('DIRECTOR')")
public class JobsController {

  private final JobUseCase jobUseCase;
  private final PaginateMapper paginateMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<JobResponse>> getJob(@PathVariable Long id) {
    return ResponseUtils.successResponse(jobUseCase.getDomain(id));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveJob(
      @Valid @RequestBody JobRequest jobRequest) {
    jobUseCase.save(jobRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateJob(
      @RequestBody JobRequest jobRequest, @PathVariable Long id) {
    jobUseCase.update(jobRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
    jobUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<JobResponse>>> getListJobsPaginate(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<JobResponse> jobPage = jobUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        jobPage.getContent(), paginateMapper.toDomain(jobPage));
  }

  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<JobResponse>>> getListJobsAll() {
    return ResponseUtils.successResponse(jobUseCase.getList());
  }
} 