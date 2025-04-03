package pe.com.graduate.insights.api.domain.models.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GraduateDto {

  private Long id;
  private String numberDocument;
  private String name;
}
