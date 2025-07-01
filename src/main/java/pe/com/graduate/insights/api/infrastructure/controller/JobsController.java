package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import pe.com.graduate.insights.api.application.ports.input.AuthUseCase;
import pe.com.graduate.insights.api.application.ports.input.JobUseCase;
import pe.com.graduate.insights.api.application.service.UserRoleService;
import pe.com.graduate.insights.api.domain.models.enums.UserRole;
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
@PreAuthorize("hasAnyRole('DIRECTOR', 'GRADUATE')")
public class JobsController {

  private final JobUseCase jobUseCase;
  private final PaginateMapper paginateMapper;
  private final AuthUseCase authUseCase;
  private final UserRoleService userRoleService;

  /**
   * Función para identificar el rol del usuario autenticado. - DIRECTOR: Ve todos los trabajos con
   * todos los atributos (incluye graduate_id) - GRADUATE: Ve solo sus trabajos sin el atributo
   * graduate_id
   */
  private boolean isUserDirector() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = authUseCase.getCurrentUser(authentication);
    UserRole userRole = userRoleService.getUserRole(user.getId());
    return userRole == UserRole.DIRECTOR;
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = authUseCase.getCurrentUser(authentication);
    return user.getId();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<JobResponse>> getJob(@PathVariable Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    JobResponse jobResponse = jobUseCase.getDomainByRole(id, isDirector, currentUserId);
    return ResponseUtils.successResponse(jobResponse);
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveJob(@Valid @RequestBody JobRequest jobRequest) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobUseCase.saveByRole(jobRequest, isDirector, currentUserId);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateJob(
      @RequestBody JobRequest jobRequest, @PathVariable Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobUseCase.updateByRole(jobRequest, id, isDirector, currentUserId);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobUseCase.deleteByRole(id, isDirector, currentUserId);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<JobResponse>>> getListJobsPaginate(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<JobResponse> jobPage =
        jobUseCase.getPaginationByRole(search, pageable, isDirector, currentUserId);
    return ResponseUtils.successResponsePaginate(
        jobPage.getContent(), paginateMapper.toDomain(jobPage));
  }

  @GetMapping("/list")
  public ResponseEntity<List<KeyValueResponse>> getListJobsAll() {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    List<KeyValueResponse> jobList = jobUseCase.getListByRole(isDirector, currentUserId);
    return new ResponseEntity<>(jobList, HttpStatus.OK);
  }
}
