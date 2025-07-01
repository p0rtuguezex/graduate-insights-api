package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.SurveyUseCase;
import pe.com.graduate.insights.api.domain.models.request.SurveyRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
public class SurveyController {

  private final SurveyUseCase surveyUseCase;
  private final PaginateMapper paginateMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<SurveyResponse>> getSurvey(@PathVariable Long id) {
    return ResponseUtils.successResponse(surveyUseCase.getDomain(id));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveSurvey(
      @Valid @RequestBody SurveyRequest surveyRequest) {
    surveyUseCase.save(surveyRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateSurvey(
      @RequestBody SurveyRequest surveyRequest, @PathVariable Long id) {
    surveyUseCase.update(surveyRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteSurvey(@PathVariable Long id) {
    surveyUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<SurveyResponse>>> getListSurveysPaginate(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size,
      @RequestParam(value = "status", required = false) String status) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<SurveyResponse> surveyPage = surveyUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        surveyPage.getContent(), paginateMapper.toDomain(surveyPage));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<ApiResponse<Void>> updateSurveyStatus(
      @PathVariable Long id, @RequestParam("status") String status) {
    surveyUseCase.updateStatus(id, status);
    return ResponseUtils.successUpdateResponse();
  }

  @GetMapping("/active")
  public ResponseEntity<ApiResponse<List<SurveyResponse>>> getActiveSurveys() {
    List<SurveyResponse> activeSurveys = surveyUseCase.getActiveSurveys();
    return ResponseUtils.successResponse(activeSurveys);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<ApiResponse<List<SurveyResponse>>> getSurveysByStatus(
      @PathVariable String status) {
    List<SurveyResponse> surveys = surveyUseCase.getSurveysByStatus(status);
    return ResponseUtils.successResponse(surveys);
  }
}
