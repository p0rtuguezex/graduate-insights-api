package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.com.graduate.insights.api.application.ports.input.GraduateSurveyResponseUseCase;
import pe.com.graduate.insights.api.domain.models.request.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateSurveyResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/survey-responses")
@RequiredArgsConstructor
@CrossOrigin(
    origins = "http://localhost:3000",
    allowCredentials = "true"
)
@PreAuthorize("hasRole('USER')")
public class GraduateSurveyResponseController {

    private final GraduateSurveyResponseUseCase graduateSurveyResponseUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveSurveyResponse(
            @Valid @RequestBody GraduateSurveyResponseRequest request) {
        graduateSurveyResponseUseCase.save(request);
        return ResponseUtils.sucessCreateResponse();
    }

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<ApiResponse<List<GraduateSurveyResponseEntity>>> getResponsesBySurveyId(
            @PathVariable Long surveyId) {
        return ResponseUtils.successResponse(graduateSurveyResponseUseCase.findBySurveyId(surveyId));
    }

    @GetMapping("/graduate/{graduateId}")
    public ResponseEntity<ApiResponse<List<GraduateSurveyResponseEntity>>> getResponsesByGraduateId(
            @PathVariable Long graduateId) {
        return ResponseUtils.successResponse(graduateSurveyResponseUseCase.findByGraduateId(graduateId));
    }
} 