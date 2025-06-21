package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.JobOffersUseCase;
import pe.com.graduate.insights.api.domain.models.request.JobOffersRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.JobOffersResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/employer/{employerId}/jobs_offers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DIRECTOR')")
public class JobOffersController {

  private final JobOffersUseCase jobOffersUseCase;

  private final PaginateMapper paginateMapper;

  @GetMapping
  public ResponseEntity<ApiResponse<List<JobOffersResponse>>> getJobsListByemployerId(
      @PathVariable Long employerId) {
    return ResponseUtils.successResponse(jobOffersUseCase.getJobsList(employerId));
  }

  @GetMapping("/{jobId}")
  public ResponseEntity<ApiResponse<JobOffersResponse>> getJobsByemployerId(
      @PathVariable Long employerId, @PathVariable Long jobId) {
    return ResponseUtils.successResponse(jobOffersUseCase.getDomain(employerId, jobId));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createJob(
      @Valid @RequestBody JobOffersRequest jobOffersRequest, @PathVariable Long employerId) {
    jobOffersUseCase.save(jobOffersRequest, employerId);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{jobId}")
  public ResponseEntity<ApiResponse<Void>> updateJob(
      @RequestBody JobOffersRequest jobOffersRequest,
      @PathVariable Long employerId,
      @PathVariable Long jobId) {
    jobOffersUseCase.update(jobOffersRequest, employerId, jobId);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{jobId}")
  public ResponseEntity<ApiResponse<Void>> deleteJob(
      @PathVariable Long employerId, @PathVariable Long jobId) {
    jobOffersUseCase.delete(employerId, jobId);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping("/pagination")
  ResponseEntity<ApiResponse<List<JobOffersResponse>>> getGraduatesJobsPaginated(
      @PathVariable Long employerId,
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<JobOffersResponse> jobOffersPage =
        jobOffersUseCase.getPagination(search, pageable, employerId);
    return ResponseUtils.successResponsePaginate(
        jobOffersPage.getContent(), paginateMapper.toDomain(jobOffersPage));
  }
}
