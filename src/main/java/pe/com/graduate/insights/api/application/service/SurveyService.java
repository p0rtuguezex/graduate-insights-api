package pe.com.graduate.insights.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.SurveyUseCase;
import pe.com.graduate.insights.api.application.ports.output.SurveyRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.SurveyRequest;
import pe.com.graduate.insights.api.domain.models.response.SurveyResponse;

@Service
@RequiredArgsConstructor
public class SurveyService implements SurveyUseCase {

  private final SurveyRepositoryPort surveyRepositoryPort;

  @Override
  public void save(SurveyRequest request) {
    surveyRepositoryPort.save(request);
  }

  @Override
  public Page<SurveyResponse> getPagination(String search, Pageable pageable) {
    return surveyRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public SurveyResponse getDomain(Long id) {
    return surveyRepositoryPort.getDomain(id);
  }

  @Override
  public void update(SurveyRequest request, Long id) {
    surveyRepositoryPort.update(request, id);
  }

  @Override
  public void delete(Long id) {
    surveyRepositoryPort.delete(id);
  }
}
