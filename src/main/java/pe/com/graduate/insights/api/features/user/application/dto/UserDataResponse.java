package pe.com.graduate.insights.api.features.user.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataResponse {
  private Long id;
  private String name;
  private String email;
  private String genero;
  private boolean verified;
  private String role;
}