package pe.com.graduate.insights.api.shared.infrastructure.advice;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import pe.com.graduate.insights.api.features.auth.domain.exception.AccountPendingApprovalException;
import pe.com.graduate.insights.api.features.auth.domain.exception.InvalidCodeException;
import pe.com.graduate.insights.api.features.auth.domain.exception.InvalidCredentialsException;
import pe.com.graduate.insights.api.features.director.domain.exception.DirectorException;
import pe.com.graduate.insights.api.features.educationcenter.domain.exception.EducationCenterException;
import pe.com.graduate.insights.api.features.emailconfig.domain.exception.EmailConfigException;
import pe.com.graduate.insights.api.features.eventtypes.domain.exception.EventTypesException;
import pe.com.graduate.insights.api.features.graduate.domain.exception.GraduateException;
import pe.com.graduate.insights.api.features.graduatesurveys.domain.exception.SurveyResponseValidationException;
import pe.com.graduate.insights.api.features.mail.domain.exception.MailException;
import pe.com.graduate.insights.api.features.storage.domain.exception.FileException;
import pe.com.graduate.insights.api.features.survey.domain.exception.SurveyException;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.shared.models.response.ApiResponse;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GraduateInsightsExceptionHandler {

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ApiResponse<List<String>>> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException ex) {
    String errorMessage = "El archivo excede el tamaño máximo permitido (10MB)";
    List<String> errors = List.of(errorMessage);
    return ResponseUtils.errorResponse(HttpStatus.BAD_REQUEST, errors);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiResponse<List<String>>> notFoundException(NotFoundException ex) {
    List<String> errors = List.of(ex.getMessage());
    return ResponseUtils.errorResponse(HttpStatus.NOT_FOUND, errors);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<List<String>>> handleDataIntegrityViolation(
      DataIntegrityViolationException ex) {
    String message = "Ya existe un registro con los datos proporcionados.";
    String cause = ex.getMostSpecificCause().getMessage();
    if (cause != null && cause.contains("correo")) {
      message = "El correo electrónico ya se encuentra registrado.";
    } else if (cause != null && cause.contains("dni")) {
      message = "El DNI ya se encuentra registrado.";
    }
    List<String> errors = List.of(message);
    return ResponseUtils.errorResponse(HttpStatus.CONFLICT, errors);
  }

  @ExceptionHandler(AccountPendingApprovalException.class)
  public ResponseEntity<ApiResponse<List<String>>> handlePendingApproval(
      AccountPendingApprovalException ex) {
    List<String> errors = List.of(ex.getMessage());
    return ResponseUtils.errorResponse(HttpStatus.FORBIDDEN, errors);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> methodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    HttpStatus status = (HttpStatus) ex.getStatusCode();
    Map<String, String> mapErrors = new HashMap<>();
    bindingResult
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              mapErrors.put(fieldName, errorMessage);
            });

    return ResponseUtils.errorResponse(status, mapErrors);
  }

  @ExceptionHandler({
    GraduateException.class,
    EventTypesException.class,
    EducationCenterException.class,
    DirectorException.class,
    SurveyException.class,
    FileException.class,
    EmailConfigException.class,
    MailException.class,
    UnknownHostException.class,
    SurveyResponseValidationException.class
  })
  public ResponseEntity<ApiResponse<List<String>>> handleDomainExceptions(RuntimeException ex) {
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    List<String> errors = List.of(ex.getMessage());
    return ResponseUtils.errorResponse(status, errors);
  }

  @ExceptionHandler(InvalidCodeException.class)
  public ResponseEntity<ApiResponse<List<String>>> handleInvalidCodeException(
      InvalidCodeException ex) {
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    List<String> errors = List.of(ex.getMessage());
    return ResponseUtils.errorResponse(status, errors);
  }

  @ExceptionHandler({
    InvalidCredentialsException.class,
    AuthenticationCredentialsNotFoundException.class,
    AuthorizationDeniedException.class,
    AuthenticationException.class
  })
  public ResponseEntity<ApiResponse<List<String>>> handleAuthenticationException(Exception ex) {
    String errorMessage;

    if (ex instanceof AuthenticationCredentialsNotFoundException) {
      errorMessage = "No se encontró información de autenticación. Por favor, inicie sesión.";
    } else if (ex instanceof AuthorizationDeniedException) {
      errorMessage = "No tiene permisos para acceder a este recurso.";
    } else if (ex instanceof InvalidCredentialsException) {
      errorMessage = "Credenciales inválidas. Verifique su usuario y contraseña.";
    } else {
      errorMessage = "Error de autenticación. Por favor, inicie sesión nuevamente.";
    }

    List<String> errors = List.of(errorMessage);
    return ResponseUtils.errorResponse(HttpStatus.UNAUTHORIZED, errors);
  }
}
