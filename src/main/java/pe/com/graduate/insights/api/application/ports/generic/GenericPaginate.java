package pe.com.graduate.insights.api.application.ports.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericPaginate<T> {
  Page<T> getPagination(String search, Pageable pageable);
}
