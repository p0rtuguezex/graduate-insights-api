package pe.com.graduate.insights.api.domain.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;

public class ResponseUtils {

  public static <T> ResponseEntity<ApiResponse<T>> createSucessResponse() {
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  public static <T> ResponseEntity<ApiResponse<T>> successResponse(T data) {
    return new ResponseEntity<>(new ApiResponse<>(data), HttpStatus.OK);
  }

  public static <T> ResponseEntity<ApiResponse<T>> errorResponse(HttpStatus httpStatus, T errors) {
    return new ResponseEntity<>(new ApiResponse<>(null, errors), httpStatus);
  }

  public static <T> ResponseEntity<ApiResponse<T>> createNotFoundResponse() {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
