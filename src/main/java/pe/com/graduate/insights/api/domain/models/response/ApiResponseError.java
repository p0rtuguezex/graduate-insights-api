package pe.com.graduate.insights.api.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseError {
  private String message;
  private String errorCode;

  public ApiResponseError(String message) {
    this.message = message;
    this.errorCode = "ERROR";
  }
}
