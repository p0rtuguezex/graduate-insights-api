package pe.com.graduate.insights.api.application.ports.generic;

public interface GenericCreateEntity<R> {

  Object saveEntity(R request);
}
