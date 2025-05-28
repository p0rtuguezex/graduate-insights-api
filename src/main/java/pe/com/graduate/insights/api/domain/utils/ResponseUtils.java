package pe.com.graduate.insights.api.domain.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.Paginate;

public class ResponseUtils {

  public static <T> ResponseEntity<ApiResponse<T>> sucessCreateResponse() {
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  public static <T> ResponseEntity<ApiResponse<T>> successResponse(T data) {
    return new ResponseEntity<>(new ApiResponse<>(data), HttpStatus.OK);
  }

  public static <T> ResponseEntity<ApiResponse<T>> successUpdateResponse() {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  public static <T> ResponseEntity<ApiResponse<T>> successDeleteResponse() {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  public static <T> ResponseEntity<ApiResponse<T>> errorResponse(HttpStatus httpStatus, T errors) {
    return new ResponseEntity<>(new ApiResponse<>(null, errors), httpStatus);
  }

  public static <T> ResponseEntity<ApiResponse<T>> createNotFoundResponse() {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  public static <T> ResponseEntity<ApiResponse<T>> successResponsePaginate(
      T data, Paginate paginate) {
    return new ResponseEntity<>(new ApiResponse<>(data, paginate), HttpStatus.OK);
  }
}
