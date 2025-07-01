package pe.com.graduate.insights.api.application.ports.output;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.domain.models.request.JobOffersRequest;
import pe.com.graduate.insights.api.domain.models.response.JobOffersResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

public interface JobOffersRepositoryPort {
  JobOffersResponse getDomain(Long id);

  void save(JobOffersRequest jobOffersRequest);

  void update(JobOffersRequest jobOffersRequest, Long id);

  void delete(Long id);

  Page<JobOffersResponse> getPagination(String search, Pageable pageable);

  List<KeyValueResponse> getList();

  JobOffersResponse getDomainByRole(Long id, boolean isDirector, Long currentUserId);

  void saveByRole(JobOffersRequest jobOffersRequest, boolean isDirector, Long currentUserId);

  void updateByRole(
      JobOffersRequest jobOffersRequest, Long id, boolean isDirector, Long currentUserId);

  void deleteByRole(Long id, boolean isDirector, Long currentUserId);

  Page<JobOffersResponse> getPaginationByRole(
      String search, Pageable pageable, boolean isDirector, Long currentUserId);
}
