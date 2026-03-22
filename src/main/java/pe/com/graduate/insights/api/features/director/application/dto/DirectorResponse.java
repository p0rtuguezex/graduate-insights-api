package pe.com.graduate.insights.api.features.director.application.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;

@SuperBuilder
@Getter
@ToString
public class DirectorResponse extends UserResponse {
  private String directorId;
}