package pe.com.graduate.insights.api.application.ports.generic;

public interface GenericRead<T> {

  T getDomain(Long id);
}
