package pe.com.graduate.insights.api.domain.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class GraduateResponse {

  private String id;
  private String name;
  private String urlSocialNetwork;
  private String description;
  private String nationality;
  private String urlImage;
}
