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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.SurveyTypeUseCase;
import pe.com.graduate.insights.api.domain.models.request.SurveyTypeRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.models.response.SurveyTypeResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/survey-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('DIRECTOR')")
public class SurveyTypeController {

  private final SurveyTypeUseCase surveyTypeUseCase;

  private final PaginateMapper paginateMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<SurveyTypeResponse>> getSurveyType(@PathVariable Long id) {
    return ResponseUtils.successResponse(surveyTypeUseCase.getDomain(id));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveSurveyType(
      @Valid @RequestBody SurveyTypeRequest surveyTypeRequest) {
    surveyTypeUseCase.save(surveyTypeRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateSurveyType(
      @RequestBody SurveyTypeRequest surveyTypeRequest, @PathVariable Long id) {
    surveyTypeUseCase.update(surveyTypeRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteSurveyType(@PathVariable Long id) {
    surveyTypeUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<SurveyTypeResponse>>> getListSurveyTypesPaginate(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<SurveyTypeResponse> surveyTypePage = surveyTypeUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        surveyTypePage.getContent(), paginateMapper.toDomain(surveyTypePage));
  }

  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<KeyValueResponse>>> getListSurveyTypeAll() {
    return ResponseUtils.successResponse(surveyTypeUseCase.getList());
  }

  @GetMapping("/active")
  public ResponseEntity<ApiResponse<List<SurveyTypeResponse>>> getActiveSurveyTypes() {
    return ResponseUtils.successResponse(surveyTypeUseCase.getActiveTypes());
  }
}
