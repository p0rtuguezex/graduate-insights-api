package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChangePasswordRequest {

  @NotNull(message = "email cannot be null")
  @NotBlank(message = "email cannot be blank")
  private String email;

  @NotNull(message = "newPassword cannot be null")
  @NotBlank(message = "newPassword cannot be blank")
  private String newPassword;
}
