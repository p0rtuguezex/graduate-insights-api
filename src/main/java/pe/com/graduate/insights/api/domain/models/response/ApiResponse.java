package pe.com.graduate.insights.api.domain.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

  @JsonProperty("data")
  private T data;

  @JsonProperty("paginate")
  private Paginate paginate;

  @JsonProperty("errors")
  private Object errors;

  private boolean success;
  private String message;
  private ApiResponseError error;

  public ApiResponse(T data) {
    this.data = data;
    this.success = true;
  }

  public ApiResponse(T data, Object errors) {
    this.data = data;
    this.errors = errors;
  }

  public ApiResponse(T data, Paginate paginate, Object errors) {
    this.data = data;
    this.paginate = paginate;
    this.errors = errors;
  }

  public ApiResponse(T data, Paginate paginate) {
    this.data = data;
    this.paginate = paginate;
  }

  public ApiResponse(Paginate paginate) {
    this.paginate = paginate;
  }

  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder()
            .success(true)
            .message("Operación exitosa")
            .data(data)
            .build();
  }

  public static <T> ApiResponse<T> success() {
    return ApiResponse.<T>builder()
            .success(true)
            .message("Operación exitosa")
            .build();
  }

  public static <T> ApiResponse<T> success(T data, String message) {
    return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
  }

  public static <T> ApiResponse<T> success(T data, Paginate paginate) {
    return ApiResponse.<T>builder()
            .success(true)
            .message("Operación exitosa")
            .data(data)
            .paginate(paginate)
            .build();
  }

  public static <T> ApiResponse<T> error(String message) {
    return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .error(new ApiResponseError(message))
            .build();
  }

  public static <T> ApiResponse<T> error(String message, String errorCode) {
    return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .error(new ApiResponseError(message, errorCode))
            .build();
  }
}
