package pe.com.graduate.insights.api.features.graduate.application.ports.output;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;

public interface GraduateReadRepositoryPort {

  List<KeyValueResponse> getList();

  Page<GraduateResponse> getPagination(String search, Pageable pageable);

  Page<GraduateResponse> getPagination(String search, Pageable pageable, Boolean validated);

  GraduateResponse getDomain(Long id);
}
