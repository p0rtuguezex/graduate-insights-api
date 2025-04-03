package pe.com.graduate.insights.api.application.ports.generic;

public interface GenericUpdate<R> {
  void update(R request, Long id);
}
