package pe.com.graduate.insights.api.domain.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class GraduateRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "name cannot be null")
  @NotBlank(message = "name cannot be blank")
  private String name;

  private String urlSocialNetwork;

  private String urlImage;

  @NotNull(message = "description cannot be null")
  @NotBlank(message = "description cannot be blank")
  private String description;

  @NotNull(message = "nationality cannot be null")
  @NotBlank(message = "nationality cannot be blank")
  private String nationality;
}
