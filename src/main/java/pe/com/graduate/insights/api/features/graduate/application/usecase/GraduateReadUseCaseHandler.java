package pe.com.graduate.insights.api.features.graduate.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.features.graduate.application.ports.input.GraduateReadUseCase;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateReadRepositoryPort;

@Service
@RequiredArgsConstructor
public class GraduateReadUseCaseHandler implements GraduateReadUseCase {

  private final GraduateReadRepositoryPort graduateReadRepositoryPort;

  @Override
  public List<KeyValueResponse> getList() {
    return graduateReadRepositoryPort.getList();
  }

  @Override
  public Page<GraduateResponse> getPagination(String search, Pageable pageable) {
    return graduateReadRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public Page<GraduateResponse> getPagination(
      String search, Pageable pageable, Boolean validated) {
    return graduateReadRepositoryPort.getPagination(search, pageable, validated);
  }

  @Override
  public GraduateResponse getDomain(Long id) {
    return graduateReadRepositoryPort.getDomain(id);
  }
}
