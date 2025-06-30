package pe.com.graduate.insights.api.domain.models.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataResponse {
  private Long id;
  private String name;
  private String email;
  private boolean verified;
  private String role;
}
