package pe.com.graduate.insights.api.shared.ports.generic;

public interface GenericCreate<R> {
  void save(R request);
}
