package pe.com.graduate.insights.api.application.ports.generic;

import java.util.List;

public interface GenericList<T> {
  List<T> getList(Object... params);
}
