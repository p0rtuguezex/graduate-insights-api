package pe.com.graduate.insights.api.domain.models.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import pe.com.graduate.insights.api.infrastructure.validation.PasswordConstraint;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelfPasswordChangeRequest {

  @NotNull(message = "newPassword no puede ser null")
  @NotBlank(message = "newPassword no puede estar vacío")
  @PasswordConstraint
  @JsonAlias({"newPassword", "new_password"})
  private String newPassword;
}
