package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.JobUseCase;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasAnyRole('GRADUATE', 'DIRECTOR')")
public class JobsController {

  private final JobUseCase jobUseCase;
  private final PaginateMapper paginateMapper;

  @GetMapping
  public ResponseEntity<ApiResponse<List<JobResponse>>> getListJobsByGraduateId(
      @RequestParam(required = false)
          @Positive(message = "El graduateId debe ser un número positivo")
          Long graduateId) {
    return ResponseUtils.successResponse(jobUseCase.getJobsList(graduateId));
  }

  @GetMapping("/{jobId}")
  public ResponseEntity<ApiResponse<JobResponse>> getJob(
      @RequestParam(required = false)
          @Positive(message = "El graduateId debe ser un número positivo")
          Long graduateId,
      @PathVariable Long jobId) {
    return ResponseUtils.successResponse(jobUseCase.getDomain(graduateId, jobId));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveJob(@Valid @RequestBody JobRequest jobRequest) {
    jobUseCase.save(jobRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{jobId}")
  public ResponseEntity<ApiResponse<Void>> updateJob(
      @RequestBody JobRequest jobRequest, @PathVariable Long jobId) {
    jobUseCase.update(jobRequest, jobId);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{jobId}")
  public ResponseEntity<ApiResponse<Void>> deleteJob(
      @RequestParam(required = false)
          @Positive(message = "El graduateId debe ser un número positivo")
          Long graduateId,
      @PathVariable Long jobId) {
    jobUseCase.delete(graduateId, jobId);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping("/pagination")
  ResponseEntity<ApiResponse<List<JobResponse>>> getListJobsPaginate(
      @RequestParam(required = false)
          @Positive(message = "El graduateId debe ser un número positivo")
          Long graduateId,
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<JobResponse> jobPage = jobUseCase.getPagination(search, pageable, graduateId);
    return ResponseUtils.successResponsePaginate(
        jobPage.getContent(), paginateMapper.toDomain(jobPage));
  }

  @GetMapping("/list")
  public ResponseEntity<List<KeyValueResponse>> getListGraduateAll() {
    return new ResponseEntity<>(jobUseCase.getJobList(), HttpStatus.OK);
  }
}
