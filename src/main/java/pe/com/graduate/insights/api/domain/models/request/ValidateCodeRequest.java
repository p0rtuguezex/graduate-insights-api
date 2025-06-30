package pe.com.graduate.insights.api.domain.models.request;

import static pe.com.graduate.insights.api.domain.utils.ConstantsUtils.RESET_PASSWORD;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ValidateCodeRequest {

  @NotNull(message = "email cannot be null")
  @NotBlank(message = "email cannot be blank")
  private String email;

  @NotNull(message = "code cannot be null")
  @NotBlank(message = "code cannot be blank")
  private String code;

  private final String type;

  public ValidateCodeRequest() {
    this.type = RESET_PASSWORD;
  }
}
