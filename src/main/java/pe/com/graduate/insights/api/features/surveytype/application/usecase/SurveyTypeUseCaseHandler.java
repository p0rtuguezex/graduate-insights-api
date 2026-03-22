package pe.com.graduate.insights.api.features.surveytype.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.features.surveytype.application.dto.SurveyTypeRequest;
import pe.com.graduate.insights.api.features.surveytype.application.dto.SurveyTypeResponse;
import pe.com.graduate.insights.api.features.surveytype.application.ports.input.SurveyTypeUseCase;
import pe.com.graduate.insights.api.features.surveytype.application.ports.output.SurveyTypeRepositoryPort;
import pe.com.graduate.insights.api.features.surveytype.domain.exception.SurveyTypeException;

@Service
@RequiredArgsConstructor
public class SurveyTypeUseCaseHandler implements SurveyTypeUseCase {

  private final SurveyTypeRepositoryPort surveyTypeRepositoryPort;

  @Override
  public void save(SurveyTypeRequest request) {
    validateSurveyTypeNameUnique(request.getName(), null);
    surveyTypeRepositoryPort.save(request);
  }

  @Override
  public SurveyTypeResponse getDomain(Long id) {
    return surveyTypeRepositoryPort.getDomain(id);
  }

  @Override
  public void update(SurveyTypeRequest request, Long id) {
    validateSurveyTypeNameUnique(request.getName(), id);
    surveyTypeRepositoryPort.update(request, id);
  }

  @Override
  public void delete(Long id) {
    surveyTypeRepositoryPort.delete(id);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return surveyTypeRepositoryPort.getList();
  }

  @Override
  public Page<SurveyTypeResponse> getPagination(String search, Pageable pageable) {
    return surveyTypeRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public List<SurveyTypeResponse> getActiveTypes() {
    return surveyTypeRepositoryPort.getActiveTypes();
  }

  private void validateSurveyTypeNameUnique(String name, Long id) {
    boolean nameExists =
        (id == null)
            ? surveyTypeRepositoryPort.existsByName(name)
            : surveyTypeRepositoryPort.existsByNameAndIdNot(name, id);

    if (nameExists) {
      throw new SurveyTypeException("Ya existe un tipo de encuesta con el nombre: " + name);
    }
  }
}

