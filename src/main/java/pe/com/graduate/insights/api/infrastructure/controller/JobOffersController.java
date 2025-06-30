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
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/jobs_offers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYER', 'DIRECTOR')")
public class JobOffersController {

  private final JobOffersUseCase jobOffersUseCase;
  private final PaginateMapper paginateMapper;

  @GetMapping("/{jobOfferId}")
  public ResponseEntity<ApiResponse<JobOffersResponse>> getJobsByemployerId(
      @RequestParam(required = false)
          @Positive(message = "El employerId debe ser un número positivo")
          Long employerId,
      @PathVariable Long jobOfferId) {
    return ResponseUtils.successResponse(jobOffersUseCase.getDomain(employerId, jobOfferId));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createJob(
      @Valid @RequestBody JobOffersRequest jobOffersRequest) {
    jobOffersUseCase.save(jobOffersRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{jobOfferId}")
  public ResponseEntity<ApiResponse<Void>> updateJob(
      @RequestBody JobOffersRequest jobOffersRequest, @PathVariable Long jobOfferId) {
    jobOffersUseCase.update(jobOffersRequest, jobOfferId);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{jobOfferId}")
  public ResponseEntity<ApiResponse<Void>> deleteJob(
      @RequestParam(required = false)
          @Positive(message = "El employerId debe ser un número positivo")
          Long employerId,
      @PathVariable Long jobOfferId) {
    jobOffersUseCase.delete(employerId, jobOfferId);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<JobOffersResponse>>> getGraduatesJobsPaginated(
      @RequestParam(required = false)
          @Positive(message = "El employerId debe ser un número positivo")
          Long employerId,
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<JobOffersResponse> jobOffersPage =
        jobOffersUseCase.getPagination(search, pageable, employerId);
    return ResponseUtils.successResponsePaginate(
        jobOffersPage.getContent(), paginateMapper.toDomain(jobOffersPage));
  }

  @GetMapping("/list")
  public ResponseEntity<List<KeyValueResponse>> getListGraduateAll() {
    return new ResponseEntity<>(jobOffersUseCase.getJobOffersList(), HttpStatus.OK);
  }
}
