package pe.com.graduate.insights.api.domain.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Paginate {
  @JsonProperty("totalElements")
  private Long totalElements;

  @JsonProperty("totalPages")
  private Integer totalPages;

  @JsonProperty("currentPage")
  private Integer currentPage;

  public static Paginate copyOf(Paginate other) {
    if (other == null) {
      return null;
    }
    return Paginate.builder()
        .totalElements(other.totalElements)
        .totalPages(other.totalPages)
        .currentPage(other.currentPage)
        .build();
  }
}
