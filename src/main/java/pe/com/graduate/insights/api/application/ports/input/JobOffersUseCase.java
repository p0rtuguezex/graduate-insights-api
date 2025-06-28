package pe.com.graduate.insights.api.application.ports.input;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.domain.models.request.JobOffersRequest;
import pe.com.graduate.insights.api.domain.models.response.JobOffersResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

public interface JobOffersUseCase {
  List<JobOffersResponse> getJobsList(Long employerId);

  JobOffersResponse getDomain(Long employerId, Long jobOfferId);

  void save(JobOffersRequest jobOffersRequest);

  void update(JobOffersRequest jobOffersRequest, Long jobOfferId);

  void delete(Long employerId, Long jobOfferId);

  Page<JobOffersResponse> getPagination(String search, Pageable pageable, Long employerId);

  List<KeyValueResponse> getJobOffersList();
}
