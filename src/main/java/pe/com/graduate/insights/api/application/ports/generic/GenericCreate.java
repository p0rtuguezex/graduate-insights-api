package pe.com.graduate.insights.api.application.ports.generic;

public interface GenericCreate<R> {
  void save(R request);
}
