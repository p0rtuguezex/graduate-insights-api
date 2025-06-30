package pe.com.graduate.insights.api.domain.models.request;

import static pe.com.graduate.insights.api.domain.utils.ConstantsUtils.RESET_PASSWORD;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class MailRequest {

  @NotNull(message = "email cannot be null")
  @NotBlank(message = "email cannot be blank")
  private String email;

  private String type;

  public MailRequest() {
    this.type = RESET_PASSWORD;
  }
}
