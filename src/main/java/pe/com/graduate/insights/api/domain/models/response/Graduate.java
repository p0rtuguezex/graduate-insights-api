package pe.com.graduate.insights.api.domain.models.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Graduate {

  private String id;
  private String name;
  private String urlSocialNetwork;
  private String description;
  private String nationality;
  private String urlImage;
}
