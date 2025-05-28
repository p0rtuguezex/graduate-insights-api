package pe.com.graduate.insights.api.application.ports.output;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.domain.models.request.JobOffersRequest;
import pe.com.graduate.insights.api.domain.models.response.JobOffersResponse;

public interface JobOffersRepositoryPort {
  List<JobOffersResponse> getJobsList(Long employerId);

  JobOffersResponse getDomain(Long employerId, Long jobOfferId);

  void save(JobOffersRequest jobOffersRequest, Long employerId);

  void update(JobOffersRequest jobOffersRequest, Long employerId, Long jobOfferId);

  void delete(Long employerId, Long jobOfferId);

  Page<JobOffersResponse> getPagination(String search, Pageable pageable, Long employerId);
}
