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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
    AuthorizationDeniedException.class
  })
  public ResponseEntity<ApiResponse<List<String>>> handleInvalidCredentialsException(Exception ex) {
    List<String> errors = List.of(ex.getMessage());
    return ResponseUtils.errorResponse(HttpStatus.UNAUTHORIZED, errors);
  }
}
