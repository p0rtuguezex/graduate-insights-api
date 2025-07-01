package pe.com.graduate.insights.api.application.ports.output;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

public interface JobRepositoryPort {
  JobResponse getDomain(Long id);

  void save(JobRequest jobRequest);

  void update(JobRequest jobRequest, Long id);

  void delete(Long id);

  Page<JobResponse> getPagination(String search, Pageable pageable);

  List<KeyValueResponse> getList();

  JobResponse getDomainByRole(Long id, boolean isDirector, Long currentUserId);

  void saveByRole(JobRequest jobRequest, boolean isDirector, Long currentUserId);

  void updateByRole(JobRequest jobRequest, Long id, boolean isDirector, Long currentUserId);

  void deleteByRole(Long id, boolean isDirector, Long currentUserId);

  Page<JobResponse> getPaginationByRole(
      String search, Pageable pageable, boolean isDirector, Long currentUserId);

  List<KeyValueResponse> getListByRole(boolean isDirector, Long currentUserId);
}
