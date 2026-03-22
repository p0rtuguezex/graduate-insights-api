package pe.com.graduate.insights.api.shared.ports.generic;

public interface GenericRead<T> {

  T getDomain(Long id);
}
