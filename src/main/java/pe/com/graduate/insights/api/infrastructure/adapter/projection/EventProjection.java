package pe.com.graduate.insights.api.infrastructure.adapter.projection;

import java.time.LocalDate;

public interface EventProjection {
  Integer getMinuteTotalEvent();

  String getName();

  String getDni();

  String getEventIds();

  LocalDate getStarDateEvenMin();

  LocalDate getEndDateEvenMax();
}
