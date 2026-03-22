package pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.shared.models.response.ApiResponse;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;
import pe.com.graduate.insights.api.features.auth.application.ports.input.AuthenticatedGraduateUseCase;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.input.GraduateSurveyResponseUseCase;
import pe.com.graduate.insights.api.features.graduatesurveys.domain.model.GraduateSurveyResponse;

@RestController
@RequestMapping("/survey-responses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('GRADUATE')")
public class GraduateSurveyResponseController {

  private final GraduateSurveyResponseUseCase graduateSurveyResponseUseCase;
  private final AuthenticatedGraduateUseCase authenticatedGraduateUseCase;

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveSurveyResponse(
      @Valid @RequestBody GraduateSurveyResponseRequest request) {
    Long graduateId = getAuthenticatedGraduateId();
    graduateSurveyResponseUseCase.save(request, graduateId);
    return ResponseUtils.sucessCreateResponse();
  }

  @GetMapping("/survey/{surveyId}")
  public ResponseEntity<ApiResponse<List<GraduateSurveyResponse>>> getResponsesBySurveyId(
      @PathVariable Long surveyId) {
    return ResponseUtils.successResponse(graduateSurveyResponseUseCase.findBySurveyId(surveyId));
  }

  @GetMapping("/graduate")
  public ResponseEntity<ApiResponse<List<GraduateSurveyResponse>>> getMyResponses() {
    Long graduateId = getAuthenticatedGraduateId();
    return ResponseUtils.successResponse(
        graduateSurveyResponseUseCase.findByGraduateId(graduateId));
  }

  private Long getAuthenticatedGraduateId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontro autenticacion");
    }
    return authenticatedGraduateUseCase.getAuthenticatedGraduateId(authentication);
  }
}

