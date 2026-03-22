package pe.com.graduate.insights.api.features.mail.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import pe.com.graduate.insights.api.shared.infrastructure.validation.PasswordConstraint;

@Builder
@Getter
public class ChangePasswordRequest {

  @NotNull(message = "email cannot be null")
  @NotBlank(message = "email cannot be blank")
  private String email;

  @NotNull(message = "code cannot be null")
  @NotBlank(message = "code cannot be blank")
  @Size(min = 6, max = 6, message = "code must contain 6 characters")
  private String code;

  @NotNull(message = "newPassword cannot be null")
  @NotBlank(message = "newPassword cannot be blank")
  @PasswordConstraint
  private String newPassword;
}

