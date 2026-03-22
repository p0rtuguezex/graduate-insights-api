package pe.com.graduate.insights.api.shared.ports.generic;

public interface GenericUpdate<R> {
  void update(R request, Long id);
}
