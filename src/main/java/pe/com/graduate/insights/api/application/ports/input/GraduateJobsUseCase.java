package pe.com.graduate.insights.api.application.ports.input;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.domain.models.request.GraduateJobRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateJobResponse;

public interface GraduateJobsUseCase {
  List<GraduateJobResponse> getJobsList(Long graduateId);

  GraduateJobResponse getDomain(Long graduateId, Long jobId);

  void save(GraduateJobRequest graduateJobRequest, Long graduateId);

  void update(GraduateJobRequest graduateJobRequest, Long graduateId, Long jobId);

  void delete(Long graduateId, Long jobId);

  Page<GraduateJobResponse> getPagination(String search, Pageable pageable, Long graduateId);
}
