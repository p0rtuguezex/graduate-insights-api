package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import pe.com.graduate.insights.api.application.ports.input.JobOffersUseCase;
import pe.com.graduate.insights.api.application.service.UserRoleService;
import pe.com.graduate.insights.api.domain.models.enums.UserRole;
import pe.com.graduate.insights.api.domain.models.request.JobOffersRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.JobOffersResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/job-offers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasAnyRole('DIRECTOR', 'EMPLOYER')")
public class JobOffersController {

  private final JobOffersUseCase jobOffersUseCase;
  private final PaginateMapper paginateMapper;
  private final AuthUseCase authUseCase;
  private final UserRoleService userRoleService;

  /**
   * Función para identificar el rol del usuario autenticado. - DIRECTOR: Ve todas las ofertas de
   * trabajo con todos los atributos (incluye employer_id) - EMPLOYER: Ve solo sus ofertas sin el
   * atributo employer_id
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
  public ResponseEntity<ApiResponse<JobOffersResponse>> getJobOffer(@PathVariable Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    JobOffersResponse jobOffersResponse =
        jobOffersUseCase.getDomainByRole(id, isDirector, currentUserId);
    return ResponseUtils.successResponse(jobOffersResponse);
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createJobOffer(
      @Valid @RequestBody JobOffersRequest jobOffersRequest) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobOffersUseCase.saveByRole(jobOffersRequest, isDirector, currentUserId);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateJobOffer(
      @RequestBody JobOffersRequest jobOffersRequest, @PathVariable Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobOffersUseCase.updateByRole(jobOffersRequest, id, isDirector, currentUserId);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteJobOffer(@PathVariable Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobOffersUseCase.deleteByRole(id, isDirector, currentUserId);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<JobOffersResponse>>> getListJobOffersPaginate(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<JobOffersResponse> jobOffersPage =
        jobOffersUseCase.getPaginationByRole(search, pageable, isDirector, currentUserId);
    return ResponseUtils.successResponsePaginate(
        jobOffersPage.getContent(), paginateMapper.toDomain(jobOffersPage));
  }
}
