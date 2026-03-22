package pe.com.graduate.insights.api.features.joboffers.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.joboffers.application.dto.JobOffersRequest;
import pe.com.graduate.insights.api.features.joboffers.application.dto.JobOffersResponse;
import pe.com.graduate.insights.api.features.joboffers.application.ports.input.JobOffersUseCase;
import pe.com.graduate.insights.api.features.joboffers.application.ports.output.JobOffersRepositoryPort;

@Service
@RequiredArgsConstructor
public class JobOffersUseCaseHandler implements JobOffersUseCase {

  private final JobOffersRepositoryPort jobOffersRepositoryPort;

  @Override
  public JobOffersResponse getDomain(Long id) {
    return jobOffersRepositoryPort.getDomain(id);
  }

  @Override
  public void save(JobOffersRequest jobOffersRequest) {
    jobOffersRepositoryPort.save(jobOffersRequest);
  }

  @Override
  public void update(JobOffersRequest jobOffersRequest, Long id) {
    jobOffersRepositoryPort.update(jobOffersRequest, id);
  }

  @Override
  public void delete(Long id) {
    jobOffersRepositoryPort.delete(id);
  }

  @Override
  public Page<JobOffersResponse> getPagination(String search, Pageable pageable) {
    return jobOffersRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public JobOffersResponse getDomainByRole(Long id, boolean isDirector, Long currentUserId) {
    return jobOffersRepositoryPort.getDomainByRole(id, isDirector, currentUserId);
  }

  @Override
  public void saveByRole(
      JobOffersRequest jobOffersRequest, boolean isDirector, Long currentUserId) {
    jobOffersRepositoryPort.saveByRole(jobOffersRequest, isDirector, currentUserId);
  }

  @Override
  public void updateByRole(
      JobOffersRequest jobOffersRequest, Long id, boolean isDirector, Long currentUserId) {
    jobOffersRepositoryPort.updateByRole(jobOffersRequest, id, isDirector, currentUserId);
  }

  @Override
  public void deleteByRole(Long id, boolean isDirector, Long currentUserId) {
    jobOffersRepositoryPort.deleteByRole(id, isDirector, currentUserId);
  }

  @Override
  public Page<JobOffersResponse> getPaginationByRole(
      String search, Pageable pageable, boolean isDirector, Long currentUserId) {
    return jobOffersRepositoryPort.getPaginationByRole(search, pageable, isDirector, currentUserId);
  }
}

