package pe.com.graduate.insights.api.application.ports.generic;

import java.util.Optional;

public interface GenericRead<T> {

  Optional<T> getDomain(Long id);
}
