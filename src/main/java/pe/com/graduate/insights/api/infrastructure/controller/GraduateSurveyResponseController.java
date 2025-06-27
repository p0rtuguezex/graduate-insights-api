package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.AuthUseCase;
import pe.com.graduate.insights.api.application.ports.input.GraduateSurveyResponseUseCase;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.GraduateSurveyResponseRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateSurveyResponseEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateRepository;

@RestController
@RequestMapping("/survey-responses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('GRADUATE')")
public class GraduateSurveyResponseController {

  private final GraduateSurveyResponseUseCase graduateSurveyResponseUseCase;
  private final AuthUseCase authUseCase;
  private final GraduateRepository graduateRepository;

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveSurveyResponse(
      @Valid @RequestBody GraduateSurveyResponseRequest request) {

    // Obtener el graduateId del usuario autenticado
    Long graduateId = getAuthenticatedGraduateId();

    // Guardar la respuesta pasando el graduateId como parámetro
    graduateSurveyResponseUseCase.save(request, graduateId);
    return ResponseUtils.sucessCreateResponse();
  }

  @GetMapping("/survey/{surveyId}")
  public ResponseEntity<ApiResponse<List<GraduateSurveyResponseEntity>>> getResponsesBySurveyId(
      @PathVariable Long surveyId) {
    return ResponseUtils.successResponse(graduateSurveyResponseUseCase.findBySurveyId(surveyId));
  }

  @GetMapping("/graduate")
  public ResponseEntity<ApiResponse<List<GraduateSurveyResponseEntity>>> getMyResponses() {
    Long graduateId = getAuthenticatedGraduateId();
    return ResponseUtils.successResponse(
        graduateSurveyResponseUseCase.findByGraduateId(graduateId));
  }

  private Long getAuthenticatedGraduateId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }

    var user = authUseCase.getCurrentUser(authentication);

    // Obtener el graduado asociado al usuario autenticado
    var graduate =
        graduateRepository
            .findByUserIdAndUserEstado(user.getId(), ConstantsUtils.STATUS_ACTIVE)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "El usuario autenticado no es un graduado o no está activo"));

    return graduate.getId();
  }
}
