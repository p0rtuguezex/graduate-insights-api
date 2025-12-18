package pe.com.graduate.insights.api.application.ports.output;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

public interface JobRepositoryPort {

  JobResponse getDomain(Long id);

  JobResponse getDomainByGraduate(Long id, Long graduateId);

  void save(JobRequest jobRequest);

  void update(JobRequest jobRequest, Long id);

  void delete(Long id);

  void deleteByGraduate(Long id, Long graduateId);

  Page<JobResponse> getPagination(String search, Pageable pageable);

  Page<JobResponse> getPaginationByGraduate(String search, Pageable pageable, Long graduateId);

  List<KeyValueResponse> getList();

  List<KeyValueResponse> getListByGraduate(Long graduateId);
}
