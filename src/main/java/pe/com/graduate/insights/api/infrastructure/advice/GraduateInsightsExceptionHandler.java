package pe.com.graduate.insights.api.infrastructure.advice;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
import pe.com.graduate.insights.api.domain.exception.DirectorException;
import pe.com.graduate.insights.api.domain.exception.EducationCenterException;
import pe.com.graduate.insights.api.domain.exception.EventTypesException;
import pe.com.graduate.insights.api.domain.exception.GraduateException;
import pe.com.graduate.insights.api.domain.exception.InvalidCredentialsException;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.exception.SurveyException;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;

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
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    List<String> errors = List.of(ex.getMessage());
    return ResponseUtils.errorResponse(status, errors);
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
    UnknownHostException.class
  })
  public ResponseEntity<ApiResponse<List<String>>> handleDomainExceptions(RuntimeException ex) {
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
